package model.statement;

import model.adt.ExeStack;
import model.adt.Dictionary;
import model.exception.StmtException;
import model.state.ProgramState;
import model.type.Type;

public class Fork implements Statement {
    private final Statement newProgram;

    public Fork(Statement newProgram) {
        this.newProgram = newProgram;
    }

    @Override
    public ProgramState execute(ProgramState state) throws StmtException {
        ExeStack<Statement> newStack = new ExeStack<>();

        return new ProgramState(newStack, state.getSymTable().deepCopy(), state.getOut(), this.newProgram, state.getFileTable(), state.getSemaphoreTable(), state.getHeap());
    }

    @Override
    public Statement deepCopy() {
        return new Fork(this.newProgram.deepCopy());
    }

    @Override
    public Dictionary<String, Type> typeCheck(Dictionary<String, Type> typeEnv) throws StmtException {
        this.newProgram.typeCheck(typeEnv.deepCopy());
        return typeEnv;
    }

    @Override
    public String toString() {
        return "Fork(" + this.newProgram + ')';
    }
}
