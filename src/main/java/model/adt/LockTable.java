package model.adt;

import model.exception.DictionaryException;

import java.util.Map;

public interface LockTable {
    int insert(Integer value);
    Integer lookup(Integer key) throws DictionaryException;
    Map<Integer, Integer> getContent();
    void set(Integer key, Integer value);
}
