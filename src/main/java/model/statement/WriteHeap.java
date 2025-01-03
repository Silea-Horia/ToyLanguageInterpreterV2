package model.statement;

import model.adt.Dictionary;
import model.exception.DictionaryException;
import model.exception.ExpressionException;
import model.exception.StmtException;
import model.expression.Expression;
import model.state.ProgramState;
import model.type.Type;
import model.type.RefType;
import model.value.Value;
import model.value.ReferenceValue;

public class WriteHeap implements Statement {
    private final String id;
    private final Expression expression;

    public WriteHeap(String id, Expression expression) {
        this.id = id;
        this.expression = expression;
    }

    @Override
    public ProgramState execute(ProgramState state) throws StmtException {
        try {
            Value value = state.getSymTable().lookup(this.id);

            ReferenceValue refValue = (ReferenceValue) value;

            Integer address = refValue.getAddress();

            if (!state.getHeap().contains(address)) {
                throw new StmtException("Address is not in HashHeap\n");
            }

            Value res = this.expression.eval(state.getSymTable(), state.getHeap());

            state.getHeap().set(address, res);

            return null;
        } catch (DictionaryException | ExpressionException e) {
            throw new StmtException(e.getMessage());
        }
    }

    @Override
    public Statement deepCopy() {
        return new WriteHeap(this.id, this.expression.deepCopy());
    }

    @Override
    public Dictionary<String, Type> typeCheck(Dictionary<String, Type> typeEnv) throws StmtException {
        try {
            Type expType = this.expression.typeCheck(typeEnv);
            Type varType = typeEnv.lookup(this.id);

            if (varType.equals(new RefType(expType))) {
                return typeEnv;
            }
            throw new StmtException("LHS and RHS of WH are not of the same type.\n");
        } catch (ExpressionException | DictionaryException e) {
            throw new StmtException(e.getMessage());
        }
    }

    @Override
    public String toString() {
        return "wh(" + this.id + ", " + this.expression + ")";
    }
}
