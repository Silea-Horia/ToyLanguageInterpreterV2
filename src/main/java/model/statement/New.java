package model.statement;

import model.adt.IDictionary;
import model.exception.DictionaryException;
import model.exception.ExpressionException;
import model.exception.StmtException;
import model.expression.IExp;
import model.state.ProgramState;
import model.type.IType;
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
            IType type = value.getType();
            if (!value.getType().equals(type)) {
                throw new StmtException("Variable is not a ref type\n");
            }

            RefValue refValue = (RefValue) value;

            IValue res = this.expression.eval(state.getSymTable(), state.getHeap());

            if (!refValue.getLocationType().equals(res.getType())) {
                throw new StmtException("The expression type is different from the reference type😅\n");
            }

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
    public IDictionary<String, IType> typeCheck(IDictionary<String, IType> typeEnv) throws StmtException {
        try {
            IType varType = typeEnv.lookup(this.id);
            IType expType = this.expression.typeCheck(typeEnv);
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
