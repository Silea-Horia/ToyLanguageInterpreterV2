package model.adt;

import model.exception.DictionaryException;

import java.util.Map;
import java.util.Set;

public interface ISymTable<K, V> {
    void insert(K k, V v);
    void remove(K k) throws DictionaryException;
    boolean contains(K k);
    V lookup(K k) throws DictionaryException;
    Set<K> keys();
    Map<K,V> getContent();
    ISymTable<K, V> deepCopy();
}
