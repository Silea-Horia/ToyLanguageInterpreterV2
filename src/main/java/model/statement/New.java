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

public class New implements Statement {
    private final String id;
    private final Expression expression;

    public New(String id, Expression expression) {
        this.id = id;
        this.expression = expression;
    }

    @Override
    public ProgramState execute(ProgramState state) throws StmtException {
        try {
            Value value = state.getSymTable().lookup(this.id);

            ReferenceValue refValue = (ReferenceValue) value;

            Value res = this.expression.eval(state.getSymTable(), state.getHeap());

            int address = state.getHeap().allocate(res);

            state.getSymTable().insert(this.id, new ReferenceValue(address, refValue.getLocationType()));

            return null;
        } catch (DictionaryException | ExpressionException e) {
            throw new StmtException(e.getMessage());
        }

    }

    @Override
    public Statement deepCopy() {
        return new New(this.id, this.expression.deepCopy());
    }

    @Override
    public Dictionary<String, Type> typeCheck(Dictionary<String, Type> typeEnv) throws StmtException {
        try {
            Type varType = typeEnv.lookup(this.id);
            Type expType = this.expression.typeCheck(typeEnv);
            if (varType.equals(new RefType(expType))) {
                return typeEnv;
            }
            throw new StmtException("RHS and LHS of NEW don't match types.\n");
        } catch (DictionaryException | ExpressionException e) {
            throw new StmtException(e.getMessage());
        }
    }

    @Override
    public String toString() {
        return "new(" + this.id + ", " + this.expression + ')';
    }
}
