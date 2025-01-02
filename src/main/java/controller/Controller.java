package controller;

import model.adt.IHeap;
import model.exception.*;
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

public class Controller {
    private Repository repository;
    private boolean displayFlag;
    private ExecutorService executor;

    public Controller(Repository repository) {
        this.repository = repository;
        this.displayFlag = true;
    }

    public void setDisplayFlag(boolean displayFlag) {
        this.displayFlag = displayFlag;
    }

    public boolean getDisplayFlag() {
        return this.displayFlag;
    }

    public int getNoPrograms() { return this.repository.getNoPrograms(); }

    public void generateInitialState(int option) throws ControllerException {
        try {
            this.repository.setState(option);
            this.logAll(this.repository.getPrgList());
        } catch (RepoException e) {
            throw new ControllerException(e.getMessage());
        }
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

    public void allStep() throws ControllerException {
        this.executor = Executors.newFixedThreadPool(2);

        List<ProgramState> prgStateList = this.removeCompletedPrg(this.repository.getPrgList());

        this.logAll(prgStateList);

        while (!prgStateList.isEmpty()) {

            this.oneStepForAll(prgStateList);

            garbageCollectAll(prgStateList);

            this.logAll(prgStateList);

            prgStateList = this.removeCompletedPrg(this.repository.getPrgList());
        }

        this.executor.shutdownNow();

        this.repository.setPrgList(prgStateList);
    }

    public void oneStep() {
        this.executor = Executors.newFixedThreadPool(2);

        List<ProgramState> prgStateList = this.removeCompletedPrg(this.repository.getPrgList());

        this.oneStepForAll(prgStateList);

        this.garbageCollectAll(prgStateList);

        this.logAll(prgStateList);

        prgStateList = this.removeCompletedPrg(this.repository.getPrgList());

        this.executor.shutdownNow();

        this.repository.setPrgList(prgStateList);
    }

    private void garbageCollectAll(List<ProgramState> programStateList) {
        ArrayList<Integer> addresses = new ArrayList<>();
        programStateList.stream().map(prgState -> getAddrFromSymTable(prgState.getSymTable().getContent().values(),
                prgState.getHeap())).forEach(addresses::addAll);

        programStateList.forEach(prgState -> prgState.getHeap().setContent(safeGarbageCollector(addresses, prgState.getHeap().getContent())));

    }

    private Map<Integer, IValue> safeGarbageCollector(List<Integer> symTableAddr, Map<Integer, IValue> heap) {
        return heap.entrySet().stream().filter(e->symTableAddr.contains(e.getKey())).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
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

    private List<ProgramState> removeCompletedPrg(List<ProgramState> prgStateList) {
        return prgStateList.stream().filter(ProgramState::isNotCompleted).collect(Collectors.toList());
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
            System.out.println(e.getMessage());
            System.exit(1);
        }


    }
}
