package model.statement;

import model.adt.IDictionary;
import model.adt.IExeStack;
import model.exception.StmtException;
import model.state.PrgState;
import model.type.IType;

public class CompStmt implements IStmt {
    private IStmt first;
    private IStmt second;

    public CompStmt(IStmt first, IStmt second) {
        this.first = first;
        this.second = second;
    }

    @Override
    public PrgState execute(PrgState state) throws StmtException {
        IExeStack<IStmt> stack = state.getExeStack();
        stack.push(this.second);
        stack.push(this.first);
        return null;
    }

    public IStmt getFirst() {
        return this.first;
    }

    public IStmt getSecond() {
        return this.second;
    }

    @Override
    public String toString() {
        return this.first + ";" + this.second;
    }

    @Override
    public IStmt deepCopy() {
        return new CompStmt(this.first.deepCopy(), this.second.deepCopy());
    }

    @Override
    public IDictionary<String, IType> typeCheck(IDictionary<String, IType> typeEnv) throws StmtException {
        return this.second.typeCheck(this.first.typeCheck(typeEnv));
    }
}
