package model.statement;

import javafx.util.Pair;
import model.adt.Dictionary;
import model.exception.DictionaryException;
import model.exception.StmtException;
import model.state.ProgramState;
import model.type.IntType;
import model.type.Type;
import model.value.IntegerValue;
import model.value.Value;

import java.util.List;

public class Acquire implements Statement {
    private final String variableId;

    public Acquire(String variableId) {
        this.variableId = variableId;
    }

    @Override
    public ProgramState execute(ProgramState state) throws StmtException {
        synchronized (state.getSemaphoreTable()) {
            try {
                Value foundIndex = state.getSymTable().lookup(this.variableId);

                if (!foundIndex.getType().equals(new IntType())) {
                    throw new StmtException("Variable is not of type int!");
                }

                Pair<Integer, List<Integer>> pair = state.getSemaphoreTable().lookup(((IntegerValue) foundIndex).getValue());

                if (pair == null) {
                    throw new StmtException("It is not an index in the semaphore table!!!!");
                }

                int listLength = pair.getValue().size();

                if (pair.getKey() > listLength) {
                    if (!pair.getValue().contains(state.getId())) {
                        pair.getValue().add(state.getId());
                    }
                } else {
                    state.getExeStack().push(this);
                }

            } catch (DictionaryException e) {
                throw new StmtException(e.getMessage());
            }

            return null;
        }
    }

    @Override
    public Statement deepCopy() {
        return new Acquire(this.variableId);
    }

    @Override
    public Dictionary<String, Type> typeCheck(Dictionary<String, Type> typeEnv) throws StmtException {
        try {
            if (typeEnv.lookup(this.variableId) instanceof IntType) {
                return typeEnv;
            }
            throw new StmtException("Variable is not of type int!");
        } catch (DictionaryException e) {
            throw new StmtException(e.getMessage());
        }
    }

    @Override
    public String toString() {
        return "acquire(" + this.variableId + ")";
    }
}
