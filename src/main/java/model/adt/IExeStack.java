package model.adt;

import model.exception.StackException;


import java.util.List;

public interface IExeStack<T> {
    void push(T t);
    T pop() throws StackException;
    int size();
    boolean isEmpty();
    List<T> getContent();
}
