package model.statement;

import model.adt.Dictionary;
import model.exception.DictionaryException;
import model.exception.StmtException;
import model.state.ProgramState;
import model.type.IntType;
import model.type.Type;
import model.value.IntegerValue;
import model.value.Value;

public class NewLock implements Statement {
    private final String variableId;

    public NewLock(String variableId) {
        this.variableId = variableId;
    }

    @Override
    public ProgramState execute(ProgramState state) throws StmtException {
        try {
            int place = state.getLockTable().insert(-1);

            Value key = state.getSymTable().lookup(this.variableId);

            if (! key.getType().equals(new IntType())) {
                throw new StmtException("NewLock: Variable is not an int type!");
            }

            state.getSymTable().insert(this.variableId, new IntegerValue(place));
        } catch (DictionaryException e) {
            throw new StmtException(e.getMessage());
        }

        return null;
    }

    @Override
    public Statement deepCopy() {
        return new NewLock(this.variableId);
    }

    @Override
    public Dictionary<String, Type> typeCheck(Dictionary<String, Type> typeEnv) throws StmtException {
        try {
            if (!typeEnv.lookup(this.variableId).equals(new IntType())) {
                throw new StmtException("NewLock: Variable is not an int type!");
            }
        } catch (DictionaryException e) {
            throw new StmtException(e.getMessage());
        }
        return typeEnv;
    }

    @Override
    public String toString() {
        return "NewLock(" + this.variableId + ")";
    }
}
