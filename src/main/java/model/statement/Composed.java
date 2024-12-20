package model.statement;

import model.adt.IDictionary;
import model.adt.IExeStack;
import model.exception.StmtException;
import model.state.ProgramState;
import model.type.IType;

public class Composed implements Statement {
    private final Statement first;
    private final Statement second;

    public Composed(Statement first, Statement second) {
        this.first = first;
        this.second = second;
    }

    @Override
    public ProgramState execute(ProgramState state) throws StmtException {
        IExeStack<Statement> stack = state.getExeStack();
        stack.push(this.second);
        stack.push(this.first);
        return null;
    }

    public Statement getFirst() {
        return this.first;
    }

    public Statement getSecond() {
        return this.second;
    }

    @Override
    public String toString() {
        return this.first + ";" + this.second;
    }

    @Override
    public Statement deepCopy() {
        return new Composed(this.first.deepCopy(), this.second.deepCopy());
    }

    @Override
    public IDictionary<String, IType> typeCheck(IDictionary<String, IType> typeEnv) throws StmtException {
        return this.second.typeCheck(this.first.typeCheck(typeEnv));
    }
}
