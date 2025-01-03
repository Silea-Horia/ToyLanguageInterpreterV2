package model.statement;

import model.adt.ExeStack;
import model.adt.IDictionary;
import model.exception.StmtException;
import model.state.ProgramState;
import model.type.IType;

public class Fork implements Statement {
    private final Statement newProgram;

    public Fork(Statement newProgram) {
        this.newProgram = newProgram;
    }

    @Override
    public ProgramState execute(ProgramState state) throws StmtException {
        ExeStack<Statement> newStack = new ExeStack<>();

        return new ProgramState(newStack, state.getSymTable().deepCopy(), state.getOut(), this.newProgram, state.getFileTable(), state.getHeap(), state.getId() + 1);
    }

    @Override
    public Statement deepCopy() {
        return new Fork(this.newProgram.deepCopy());
    }

    @Override
    public IDictionary<String, IType> typeCheck(IDictionary<String, IType> typeEnv) throws StmtException {
        this.newProgram.typeCheck(typeEnv.deepCopy());
        return typeEnv;
    }

    @Override
    public String toString() {
        return "Fork(" + this.newProgram + ')';
    }
}
