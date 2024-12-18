package model.statement;

import model.adt.ExeStack;
import model.adt.IDictionary;
import model.exception.StmtException;
import model.state.PrgState;
import model.type.IType;

public class ForkStmt implements IStmt {
    private IStmt stmt;

    public ForkStmt(IStmt stmt) {
        this.stmt = stmt;
    }

    @Override
    public PrgState execute(PrgState state) throws StmtException {
        ExeStack<IStmt> newStack = new ExeStack<>();

        return new PrgState(newStack, state.getSymTable().deepCopy(), state.getOut(), this.stmt, state.getFileTable(), state.getHeap());
    }

    @Override
    public IStmt deepCopy() {
        return new ForkStmt(this.stmt.deepCopy());
    }

    @Override
    public IDictionary<String, IType> typeCheck(IDictionary<String, IType> typeEnv) throws StmtException {
        this.stmt.typeCheck(typeEnv.deepCopy());
        return typeEnv;
    }

    @Override
    public String toString() {
        return "Fork(" + this.stmt + ')';
    }
}
