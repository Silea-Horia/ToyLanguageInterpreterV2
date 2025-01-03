package model.statement;

import model.adt.IDictionary;
import model.adt.IOutList;
import model.exception.ExpressionException;
import model.exception.StmtException;
import model.expression.IExp;
import model.state.ProgramState;
import model.type.Type;
import model.value.IValue;

public class Print implements Statement {
    private final IExp expression;

    public Print(IExp expression) {
        this.expression = expression;
    }

    @Override
    public ProgramState execute(ProgramState state) throws StmtException {
        IOutList<IValue> list = state.getOut();
        try {
            list.add(this.expression.eval(state.getSymTable(), state.getHeap()));
        } catch (ExpressionException e) {
            throw new StmtException(e.getMessage());
        }
        return null;
    }

    @Override
    public String toString() {
        return "print(" + this.expression + ")";
    }

    @Override
    public Statement deepCopy() {
        return new Print(this.expression.deepCopy());
    }

    @Override
    public IDictionary<String, Type> typeCheck(IDictionary<String, Type> typeEnv) throws StmtException {
        try {
            this.expression.typeCheck(typeEnv);
        } catch (ExpressionException e) {
            throw new StmtException(e.getMessage());
        }
        return typeEnv;
    }
}
