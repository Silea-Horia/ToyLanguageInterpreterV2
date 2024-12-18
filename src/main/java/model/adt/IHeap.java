package model.adt;

import model.exception.DictionaryException;
import model.value.IValue;

import java.util.Map;
import java.util.Set;

public interface IHeap {
    Integer allocate(IValue value);
    void deallocate(Integer address) throws DictionaryException;
    boolean contains(Integer address);
    IValue getValue(Integer address) throws DictionaryException;
    Set<Integer> addresses();
    void set(Integer address, IValue newValue) throws DictionaryException;
    Map<Integer, IValue> getContent();
    void setContent(Map<Integer, IValue> newContent);
}
