package com.example.toylanguageinterpreter;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.Pair;
import model.adt.Heap;
import model.exception.DictionaryException;
import model.exception.RepoException;
import model.state.ProgramState;
import model.statement.Statement;
import model.value.Value;
import model.value.ReferenceValue;
import model.value.StringValue;
import repository.Repository;

import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class ProgramLogic {
    private final Repository repository;
    private ExecutorService executor;

    @FXML
    private TextField noPrograms;

    @FXML
    private ListView<Statement> executionStack;

    @FXML
    private TableView<Pair<Integer, String>> heap;
    @FXML
    private TableColumn<Pair<Integer, String>, Integer> heapAddress;
    @FXML
    private TableColumn<Pair<Integer, String>, String> heapValue;

    @FXML
    private ListView<Value> out;

    @FXML
    private TableView<Pair<String, Value>> symbolTable;
    @FXML
    private TableColumn<Pair<String, Value>, String> stackID;
    @FXML
    private TableColumn<Pair<String, Value>, String> stackValue;

    @FXML
    private ListView<StringValue> fileTable;

    @FXML
    private ListView<ProgramState> programStates;

    @FXML
    private Button oneStep;


    public ProgramLogic(Repository repository) {
        this.repository = repository;
    }

    @FXML
    public void initialize() {
        // exe stack
        this.executionStack.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Statement item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.toString());
                }
            }
        });

        // sym table
        this.stackID.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getKey()));
        this.stackValue.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getValue().toString()));


        // heap
        this.heapAddress.setCellValueFactory(param -> new SimpleIntegerProperty(param.getValue().getKey()).asObject());
        this.heapValue.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getValue()));

        this.out.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Value item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.toString());
                }
            }
        });

        this.fileTable.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(StringValue item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getValue());
                }
            }
        });

        this.programStates.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(ProgramState item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getId().toString());
                }
            }
        });

        this.updateWindow();
    }

    @FXML
    protected void runOneStep() {
        this.executor = Executors.newFixedThreadPool(2);

        List<ProgramState> prgStateList = this.removeCompletedPrg(this.repository.getPrgList());

        this.logAll(prgStateList);

        this.oneStepForAll(prgStateList);

        garbageCollectAll(prgStateList);

        this.logAll(prgStateList);

        this.updateWindow();

        prgStateList = this.removeCompletedPrg(this.repository.getPrgList());

        this.executor.shutdownNow();

        this.repository.setPrgList(prgStateList);

        this.updateWindow();
    }

    private List<ProgramState> removeCompletedPrg(List<ProgramState> prgStateList) {
        return prgStateList.stream().filter(ProgramState::isNotCompleted).collect(Collectors.toList());
    }

    private void logAll(List<ProgramState> prgStateList) {
        prgStateList.forEach(prg -> {
            try {
                this.repository.logPrgState(prg);
            } catch (RepoException e) {
                System.out.println(e.getMessage());
                System.exit(1);
            }
        });
    }

    private void oneStepForAll(List<ProgramState> programStateList) {
        List<Callable<ProgramState>> callList = programStateList.stream().map((ProgramState p) -> (Callable<ProgramState>)(p::oneStep)).toList();

        try {
            List<ProgramState> newPrgList = this.executor.invokeAll(callList).stream().map(future ->
            {
                try {
                    return future.get();
                } catch (InterruptedException | ExecutionException re) {
                    System.out.println(re.getMessage()); System.exit(1);
                }
                return null;
            }).filter(Objects::nonNull).toList();

            programStateList.addAll(newPrgList);

            this.repository.setPrgList(programStateList);

        } catch (InterruptedException e) {
            // TODO error
            System.out.println(e.getMessage());
            System.exit(1);
        }
    }

    private void garbageCollectAll(List<ProgramState> programStateList) {
        ArrayList<Integer> addresses = new ArrayList<>();
        programStateList.stream().map(prgState -> getAddrFromSymTable(prgState.getSymTable().getContent().values(),
                prgState.getHeap())).forEach(addresses::addAll);

        programStateList.forEach(prgState -> prgState.getHeap().setContent(safeGarbageCollector(addresses, prgState.getHeap().getContent())));

    }

    private List<Integer> getAddrFromSymTable(Collection<Value> symTableValues, Heap heap) {
        return symTableValues.stream()
                .filter(v -> v instanceof ReferenceValue)
                .map(v -> (ReferenceValue)v)
                .flatMap(
                        v->{
                            List<Integer> addresses = new ArrayList<>();
                            while (true) {
                                if (v.getAddress() == 0) {
                                    break;
                                }
                                addresses.add(v.getAddress());
                                try {
                                    Value next = heap.getValue(v.getAddress());
                                    if (next instanceof ReferenceValue) {
                                        v = (ReferenceValue) next;
                                    } else break;
                                } catch (DictionaryException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                            return addresses.stream();
                        }
                ).collect(Collectors.toList());
    }

    private Map<Integer, Value> safeGarbageCollector(List<Integer> symTableAddr, Map<Integer, Value> heap) {
        return heap.entrySet().stream().filter(e->symTableAddr.contains(e.getKey())).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private void updateWindow() {
        this.updateNoPrograms();
        this.updateProgramStates();

        if (this.repository.getPrgList().isEmpty()) {
            this.oneStep.setDisable(true);
            return;
        }

        this.updateProgramInfo();

        this.updateHeap();

        this.updateOut();

        this.updateFileTable();
    }

    private void updateNoPrograms() {
        this.noPrograms.setText(Integer.toString(this.repository.getNoPrograms()));
    }

    @FXML
    protected void updateProgramInfo() {
        if (!this.repository.getPrgList().isEmpty()) {
            this.updateExecutionStack();
            this.updateSymbolTable();
        }
    }

    private void updateSymbolTable() {
        ObservableList<Pair<String, Value>> data = FXCollections.observableArrayList(this.repository.getPrgList().getFirst().getSymTable().getContent().entrySet().stream().map(p -> new Pair<>(p.getKey(), p.getValue())).collect(Collectors.toList()));

        this.symbolTable.setItems(data);
    }

    private void updateExecutionStack() {
        if (!this.programStates.getSelectionModel().getSelectedIndices().isEmpty())
            this.executionStack.setItems(FXCollections.observableArrayList(this.repository.getPrgList().get(this.programStates.getSelectionModel().getSelectedIndices().getFirst()).getExeStack().getContent()));
    }

    private void updateHeap() {
        ObservableList<Pair<Integer, String>> data = FXCollections.observableArrayList(this.repository.getPrgList().getFirst().getHeap().getContent().entrySet().stream().map(p -> new Pair<>(p.getKey(), p.getValue().toString())).collect(Collectors.toList()));

        this.heap.setItems(data);
    }

    private void updateOut() {
        this.out.setItems(FXCollections.observableArrayList(this.repository.getPrgList().getFirst().getOut().getAll()));
    }

    private void updateFileTable() {
        this.fileTable.setItems(FXCollections.observableArrayList(this.repository.getPrgList().getFirst().getFileTable().keys()));
    }

    private void updateProgramStates() {
        this.programStates.setItems(FXCollections.observableArrayList(this.repository.getPrgList()));
    }
}
