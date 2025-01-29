package model.statement;

import model.adt.Dictionary;
import model.exception.DictionaryException;
import model.exception.StmtException;
import model.state.ProgramState;
import model.type.IntType;
import model.type.Type;
import model.value.IntegerValue;
import model.value.Value;

public class Unlock implements Statement {
    private final String variableId;

    public Unlock(String variableId) {
        this.variableId = variableId;
    }

    @Override
    public ProgramState execute(ProgramState state) throws StmtException {
        try {
            Value foundIndex = state.getSymTable().lookup(this.variableId);

            if (!foundIndex.getType().equals(new IntType())) {
                throw new StmtException("Unlock: Variable is not an int type!");
            }

            Integer result = state.getLockTable().lookup(((IntegerValue)foundIndex).getValue());

            if (result == null) {
                throw new StmtException("Unlock: Variable is not assigned to a lock!");
            }

            if (result.equals(state.getId())) {
                state.getLockTable().set(((IntegerValue)foundIndex).getValue(), -1);
            }
        } catch (DictionaryException e) {
            throw new StmtException(e.getMessage());
        }

        return null;
    }

    @Override
    public Statement deepCopy() {
        return new Unlock(this.variableId);
    }

    @Override
    public Dictionary<String, Type> typeCheck(Dictionary<String, Type> typeEnv) throws StmtException {
        try {
            if (!typeEnv.lookup(this.variableId).equals(new IntType())) {
                throw new StmtException("Unlock: Variable is not an int type!");
            }
        } catch (DictionaryException e) {
            throw new StmtException(e.getMessage());
        }
        return typeEnv;
    }

    @Override
    public String toString() {
        return "Unlock(" + this.variableId + ")";
    }
}
