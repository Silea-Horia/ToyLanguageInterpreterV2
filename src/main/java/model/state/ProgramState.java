package model.state;

import model.adt.*;
import model.exception.StackException;
import model.exception.StateException;
import model.exception.StmtException;
import model.statement.Composed;
import model.statement.Statement;
import model.value.Value;
import model.value.StringValue;

import java.io.BufferedReader;

public class ProgramState {
    private final IExeStack<Statement> exeStack;
    private final Dictionary<String, Value> symTable;
    private final IOutList<Value> out;
    private final Dictionary<StringValue, BufferedReader> fileTable;
    private final SemaphoreTable semaphoreTable;
    private final LatchTable latchTable;
    private final LockTable lockTable;
    private final Heap heap;
    private final Integer id;
    public static Integer nextId = 0;

    public ProgramState(IExeStack<Statement> exeStack, Dictionary<String, Value> symTable, IOutList<Value> out, Statement originalProgram, Dictionary<StringValue, BufferedReader> fileTable, SemaphoreTable semaphoreTable, LatchTable latchTable, LockTable lockTable, Heap heap) {
        this.exeStack = exeStack;
        this.symTable = symTable;
        this.out = out;
        this.semaphoreTable = semaphoreTable;
        this.latchTable = latchTable;
        this.lockTable = lockTable;
        this.heap = heap;
        this.convertToStack(originalProgram);
        this.fileTable = fileTable;
        this.id = nextId;
        nextId += 1;
    }

    private void convertToStack(Statement stmt) {
        Statement first, second;

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

    public IOutList<Value> getOut() { return this.out; }

    public Integer getId() {return this.id;}

    public Heap getHeap() { return this.heap; }

    public LockTable getLockTable() {
        return this.lockTable;
    }

    public SemaphoreTable getSemaphoreTable() {
        return this.semaphoreTable;
    }

    public LatchTable getLatchTable() {
        return this.latchTable;
    }

    public Dictionary<String, Value> getSymTable() {
        return this.symTable;
    }

    public Dictionary<StringValue, BufferedReader> getFileTable() {
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
