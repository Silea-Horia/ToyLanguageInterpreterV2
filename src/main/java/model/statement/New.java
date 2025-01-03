package model.statement;

import model.adt.IDictionary;
import model.exception.DictionaryException;
import model.exception.ExpressionException;
import model.exception.StmtException;
import model.expression.IExp;
import model.state.ProgramState;
import model.type.Type;
import model.type.RefType;
import model.value.IValue;
import model.value.RefValue;

public class New implements Statement {
    private final String id;
    private final IExp expression;

    public New(String id, IExp expression) {
        this.id = id;
        this.expression = expression;
    }

    @Override
    public ProgramState execute(ProgramState state) throws StmtException {
        try {
            IValue value = state.getSymTable().lookup(this.id);

            RefValue refValue = (RefValue) value;

            IValue res = this.expression.eval(state.getSymTable(), state.getHeap());

            int address = state.getHeap().allocate(res);

            state.getSymTable().insert(this.id, new RefValue(address, refValue.getLocationType()));

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
    public IDictionary<String, Type> typeCheck(IDictionary<String, Type> typeEnv) throws StmtException {
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
