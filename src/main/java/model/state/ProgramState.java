package model.state;

import model.adt.*;
import model.exception.StackException;
import model.exception.StateException;
import model.exception.StmtException;
import model.statement.Composed;
import model.statement.Statement;
import model.value.IValue;
import model.value.StringValue;

import java.io.BufferedReader;

public class ProgramState {
    private final IExeStack<Statement> exeStack;
    private final ISymTable<String, IValue> symTable;
    private final IOutList<IValue> out;
    private final IFileTable<StringValue, BufferedReader> fileTable;
    private final IHeap heap;
    private Integer id;
    private static int lastId = 0;  // TODO reset on new program

    public ProgramState(IExeStack<Statement> exeStack, ISymTable<String, IValue> symTable, IOutList<IValue> out, Statement originalProgram, IFileTable<StringValue, BufferedReader> fileTable, IHeap heap) {
        this.exeStack = exeStack;
        this.symTable = symTable;
        this.out = out;
        this.heap = heap;
        this.convertToStack(originalProgram);
        this.fileTable = fileTable;
        this.setId();
    }

    private synchronized void setId() {
        this.id = lastId++;
    }

    private void convertToStack(Statement stmt) {
        Statement first, second;
        //stmt.toString().startsWith("comp")
        if (stmt instanceof Composed) {
            first = ((Composed)stmt).getFirst();
            second = ((Composed)stmt).getSecond();

            if (second instanceof Composed) {
                this.convertToStack(second);
            } else {
                this.exeStack.push(second);
            }

            if (first instanceof Composed) {
                this.convertToStack(first);
            } else {
                this.exeStack.push(first);
            }
        } else {
            this.exeStack.push(stmt);
        }
    }

    public IExeStack<Statement> getExeStack() { return this.exeStack; }

    public IOutList<IValue> getOut() { return this.out; }

    public Integer getId() {return this.id;}

    public IHeap getHeap() { return this.heap; }

    public ISymTable<String, IValue> getSymTable() {
        return this.symTable;
    }

    public IFileTable<StringValue, BufferedReader> getFileTable() {
        return this.fileTable;
    }

    public boolean isNotCompleted() {
        return !this.exeStack.isEmpty();
    }

    public ProgramState oneStep() throws StateException {
        try {
            Statement crt = this.exeStack.pop();
            return crt.execute(this);
        } catch (StackException | StmtException e) {
            throw new StateException(e.getMessage());
        }
    }

    @Override
    public String toString() {
        return "ProgramState no. " + this.id + " is:\n" + this.exeStack + this.symTable + this.out + this.fileTable + this.heap;
    }
}
