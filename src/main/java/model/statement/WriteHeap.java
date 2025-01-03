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

public class WriteHeap implements Statement {
    private final String id;
    private final IExp expression;

    public WriteHeap(String id, IExp expression) {
        this.id = id;
        this.expression = expression;
    }

    @Override
    public ProgramState execute(ProgramState state) throws StmtException {
        try {
            IValue value = state.getSymTable().lookup(this.id);

            RefValue refValue = (RefValue) value;

            Integer address = refValue.getAddress();

            if (!state.getHeap().contains(address)) {
                throw new StmtException("Address is not in Heap\n");
            }

            IValue res = this.expression.eval(state.getSymTable(), state.getHeap());

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
    public IDictionary<String, Type> typeCheck(IDictionary<String, Type> typeEnv) throws StmtException {
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
