package model.adt;

import model.exception.DictionaryException;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class FileTable<K, V> implements Dictionary<K, V> {
    private final Map<K, V> map;

    public FileTable() {
        this.map = new HashMap<>();
    }

    @Override
    public void insert(K k, V v) {
        this.map.put(k, v);
    }

    @Override
    public void remove(K k) throws DictionaryException {
        if (!this.map.containsKey(k)) throw new DictionaryException("FileTable: Key doesn't exist");
        this.map.remove(k);
    }

    @Override
    public boolean contains(K k) {
        return this.map.containsKey(k);
    }

    @Override
    public V lookup(K k) throws DictionaryException {
        if (!this.map.containsKey(k)) throw new DictionaryException("FileTable: Key doesn't exist");
        return this.map.get(k);
    }

    @Override
    public Set<K> keys() {
        return this.map.keySet();
    }

    @Override
    public Map<K, V> getContent() {
        return this.map;
    }

    @Override
    public Dictionary<K, V> deepCopy() {
        FileTable<K, V> newTable = new FileTable<>();
        this.map.forEach(newTable::insert);
        return newTable;
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        for (K k : this.map.keySet()) {
            str.append(k).append("\n");
        }
        return "FileTable:\n" + str;
    }
}
