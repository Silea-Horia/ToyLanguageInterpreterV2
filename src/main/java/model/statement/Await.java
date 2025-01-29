package model.statement;

import model.adt.Dictionary;
import model.exception.DictionaryException;
import model.exception.StmtException;
import model.state.ProgramState;
import model.type.IntType;
import model.type.Type;
import model.value.IntegerValue;
import model.value.Value;

public class Await implements Statement {
    private final String variableId;

    public Await(String variableId) {
        this.variableId = variableId;
    }

    @Override
    public ProgramState execute(ProgramState state) throws StmtException {
        try {
            Value key = state.getSymTable().lookup(this.variableId);

            if (!key.getType().equals(new IntType())) {
                throw new StmtException("Await: Variable is not an int type!");
            }

            Integer value = state.getLatchTable().lookup(((IntegerValue)key).getValue());

            if (value == null) {
                throw new StmtException("Await: Index not found in the LatchTable!");
            }

            if (value != 0) {
                state.getExeStack().push(this);
            }

            return null;
        } catch (DictionaryException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Statement deepCopy() {
        return new Await(this.variableId);
    }

    @Override
    public Dictionary<String, Type> typeCheck(Dictionary<String, Type> typeEnv) throws StmtException {
        try {
            if (! (typeEnv.lookup(this.variableId) instanceof IntType)) {
                throw new StmtException("Await: Variable is not an int type!");
            }

            return typeEnv;
        } catch (DictionaryException e) {
            throw new StmtException(e.getMessage());
        }
    }

    @Override
    public String toString() {
        return "Await(" + this.variableId + ")";
    }
}
