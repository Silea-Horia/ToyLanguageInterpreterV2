package model.statement;

import model.adt.Dictionary;
import model.exception.DictionaryException;
import model.exception.ExpressionException;
import model.exception.StmtException;
import model.expression.Expression;
import model.state.ProgramState;
import model.type.IntType;
import model.type.Type;
import model.value.IntegerValue;
import model.value.Value;

public class NewLatch implements Statement {
    private final String variableId;
    private final Expression count;

    public NewLatch(String variableId, Expression count) {
        this.variableId = variableId;
        this.count = count;
    }

    @Override
    public ProgramState execute(ProgramState state) throws StmtException {
        try {
            Value number = this.count.eval(state.getSymTable(), state.getHeap());

            if (!number.getType().equals(new IntType())) {
                throw new StmtException("NewLatch: Expression is not an int type!");
            }

            int place = state.getLatchTable().insert(((IntegerValue)number).getValue());

            Dictionary<String, Value> stack = state.getSymTable();

            if (stack.contains(this.variableId) && stack.lookup(this.variableId).getType().equals(new IntType())) {
                stack.insert(this.variableId, new IntegerValue(place));
                return null;
            }

            throw new StmtException("NewLatch: Variable doesn't exist or it isn't an int type!");
        } catch (ExpressionException | DictionaryException e) {
            throw new StmtException(e.getMessage());
        }
    }

    @Override
    public Statement deepCopy() {
        return new NewLatch(this.variableId, this.count.deepCopy());
    }

    @Override
    public Dictionary<String, Type> typeCheck(Dictionary<String, Type> typeEnv) throws StmtException {
        try {
            if (! (typeEnv.lookup(this.variableId) instanceof IntType)) {
                throw new StmtException("NewLatch: Variable is not an int type!");
            }

            if (!this.count.typeCheck(typeEnv).equals(new IntType())) {
                throw new StmtException("NewLatch: Expression is not an int type!");
            }

            return typeEnv;
        } catch (DictionaryException | ExpressionException e) {
            throw new StmtException(e.getMessage());
        }
    }

    @Override
    public String toString() {
        return "NewLatch(" + this.variableId +", " + this.count + ")";
    }
}
