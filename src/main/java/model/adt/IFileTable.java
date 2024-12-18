package model.adt;

import model.exception.DictionaryException;

import java.util.Set;

public interface IFileTable<K, V> {
    void insert(K k, V v);
    void remove(K k) throws DictionaryException;
    boolean contains(K k);
    V lookup(K k) throws DictionaryException;
    Set<K> keys();
}
