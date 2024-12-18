package model.statement;

import model.adt.IDictionary;
import model.adt.IOutList;
import model.exception.ExpressionException;
import model.exception.StmtException;
import model.expression.IExp;
import model.state.PrgState;
import model.type.IType;
import model.value.IValue;

public class PrintStmt implements IStmt {
    IExp exp;

    public PrintStmt(IExp exp) {
        this.exp = exp;
    }

    @Override
    public PrgState execute(PrgState state) throws StmtException {
        IOutList<IValue> list = state.getOut();
        try {
            list.add(this.exp.eval(state.getSymTable(), state.getHeap()));
        } catch (ExpressionException e) {
            throw new StmtException(e.getMessage());
        }
        return null;
    }

    @Override
    public String toString() {
        return "print(" + this.exp + ")";
    }

    @Override
    public IStmt deepCopy() {
        return new PrintStmt(this.exp.deepCopy());
    }

    @Override
    public IDictionary<String, IType> typeCheck(IDictionary<String, IType> typeEnv) throws StmtException {
        try {
            this.exp.typeCheck(typeEnv);
        } catch (ExpressionException e) {
            throw new StmtException(e.getMessage());
        }
        return typeEnv;
    }
}
