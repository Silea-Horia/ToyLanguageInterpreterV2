package model.adt;

import model.exception.DictionaryException;

import java.util.HashMap;
import java.util.Map;

public class LockTableImpl implements LockTable {
    private final Map<Integer, Integer> table;
    private int lastFree;

    public LockTableImpl() {
        this.table = new HashMap<>();
        this.lastFree = 0;
    }

    private void getNextFree() {
        int current = 0;
        while (this.table.containsKey(current)) current++;
        this.lastFree = current;
    }

    @Override
    synchronized public int insert(Integer value) {
        this.getNextFree();
        this.table.put(this.lastFree, value);
        return this.lastFree;
    }

    @Override
    synchronized public Integer lookup(Integer key) throws DictionaryException {
        return this.table.get(key);
    }

    @Override
    public Map<Integer, Integer> getContent() {
        return this.table;
    }

    @Override
    synchronized public void set(Integer key, Integer value) {
        this.table.put(key, value);
    }
}
