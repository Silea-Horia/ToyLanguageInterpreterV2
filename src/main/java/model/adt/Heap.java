package model.adt;

import model.exception.DictionaryException;
import model.value.Value;

import java.util.Map;
import java.util.Set;

public interface Heap {
    Integer allocate(Value value);
    void deallocate(Integer address) throws DictionaryException;
    boolean contains(Integer address);
    Value getValue(Integer address) throws DictionaryException;
    Set<Integer> addresses();
    void set(Integer address, Value newValue) throws DictionaryException;
    Map<Integer, Value> getContent();
    void setContent(Map<Integer, Value> newContent);
}
