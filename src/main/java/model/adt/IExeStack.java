package model.adt;

import model.exception.StackException;

public interface IExeStack<T> {
    void push(T t);
    T pop() throws StackException;
    int size();
    boolean isEmpty();
}
