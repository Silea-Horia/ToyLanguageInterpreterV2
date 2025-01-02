package com.example.toylanguageinterpreter;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import model.adt.IHeap;
import model.exception.DictionaryException;
import model.exception.RepoException;
import model.state.ProgramState;
import model.value.IValue;
import model.value.RefValue;
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
    private Button oneStep;

    public ProgramLogic(Repository repository) {
        this.repository = repository;
    }

    @FXML
    public void initialize() {
        this.updateNoPrograms();
    }

    private void updateNoPrograms() {
        this.noPrograms.setText(Integer.toString(this.repository.getNoPrograms()));
    }

    @FXML
    protected void runOneStep() {
        this.executor = Executors.newFixedThreadPool(2);

        List<ProgramState> prgStateList = this.removeCompletedPrg(this.repository.getPrgList());

        this.logAll(prgStateList);

        this.oneStepForAll(prgStateList);

        garbageCollectAll(prgStateList);

        this.logAll(prgStateList);

        prgStateList = this.removeCompletedPrg(this.repository.getPrgList());

        this.executor.shutdownNow();

        this.repository.setPrgList(prgStateList);

        if (prgStateList.isEmpty()) {
            this.oneStep.setDisable(true);
        }
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

    public void oneStepForAll(List<ProgramState> programStateList) {
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

    private List<Integer> getAddrFromSymTable(Collection<IValue> symTableValues, IHeap heap) {
        return symTableValues.stream()
                .filter(v -> v instanceof RefValue)
                .map(v -> (RefValue)v)
                .flatMap(
                        v->{
                            List<Integer> addresses = new ArrayList<>();
                            while (true) {
                                if (v.getAddress() == 0) {
                                    break;
                                }
                                addresses.add(v.getAddress());
                                try {
                                    IValue next = heap.getValue(v.getAddress());
                                    if (next instanceof RefValue) {
                                        v = (RefValue) next;
                                    } else break;
                                } catch (DictionaryException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                            return addresses.stream();
                        }
                ).collect(Collectors.toList());
    }

    private Map<Integer, IValue> safeGarbageCollector(List<Integer> symTableAddr, Map<Integer, IValue> heap) {
        return heap.entrySet().stream().filter(e->symTableAddr.contains(e.getKey())).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

}
