package model.adt;

import model.exception.StackException;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class ExeStack<T> implements IExeStack<T> {
    private final Deque<T> stack;

    public ExeStack() { this.stack = new ArrayDeque<>(); }

    @Override
    public void push(T t) {
        this.stack.push(t);
    }

    @Override
    public T pop() throws StackException {
        if (this.stack.isEmpty()) throw new StackException("ExeStack is empty!\n");
        return this.stack.pop();
    }

    @Override
    public int size() {
        return this.stack.size();
    }

    @Override
    public boolean isEmpty() {
        return this.stack.isEmpty();
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        this.stack.forEach((t) -> str.append(t).append(";\n"));
        return "ExeStack:\n" + str;
    }

    @Override
    public List<T> getContent() {
        return new ArrayList<>(this.stack);
    }
}
