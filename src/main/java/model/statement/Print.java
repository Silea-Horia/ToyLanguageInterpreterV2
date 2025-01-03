package model.statement;

import model.adt.Dictionary;
import model.adt.IOutList;
import model.exception.ExpressionException;
import model.exception.StmtException;
import model.expression.Expression;
import model.state.ProgramState;
import model.type.Type;
import model.value.Value;

public class Print implements Statement {
    private final Expression expression;

    public Print(Expression expression) {
        this.expression = expression;
    }

    @Override
    public ProgramState execute(ProgramState state) throws StmtException {
        IOutList<Value> list = state.getOut();
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
    public Dictionary<String, Type> typeCheck(Dictionary<String, Type> typeEnv) throws StmtException {
        try {
            this.expression.typeCheck(typeEnv);
        } catch (ExpressionException e) {
            throw new StmtException(e.getMessage());
        }
        return typeEnv;
    }
}
