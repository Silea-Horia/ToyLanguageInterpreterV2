package model.adt;

import model.exception.DictionaryException;
import model.value.Value;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class HashHeap implements Heap {
    private int nextFree;
    private Map<Integer, Value> memory;

    public HashHeap() {
        this.nextFree = 1;
        this.memory = new HashMap<>();
    }

    private void getNextFree() {
        this.nextFree = 1;
        while (this.memory.containsKey(this.nextFree)) {
            this.nextFree += 1;
        }
    }

    @Override
    public Integer allocate(Value value) {
        int addr = this.nextFree;
        this.memory.put(this.nextFree, value);
        this.getNextFree();
        return addr;
    }

    @Override
    public void deallocate(Integer address) throws DictionaryException {
        if (!this.memory.containsKey(address)) {
            throw new DictionaryException("HashHeap address is not instantiated!\n");
        }
        this.memory.remove(address);
        if (address < this.nextFree) this.nextFree = address;
    }

    @Override
    public boolean contains(Integer address) {
        return this.memory.containsKey(address);
    }

    @Override
    public Value getValue(Integer address) throws DictionaryException {
        if (!this.memory.containsKey(address)) {
            throw new DictionaryException("HashHeap address is not instantiated!\n");
        }
        return this.memory.get(address);
    }

    @Override
    public Set<Integer> addresses() {
        return this.memory.keySet();
    }

    @Override
    public void set(Integer address, Value newValue) throws DictionaryException {
        if (!this.memory.containsKey(address)) {
            throw new DictionaryException("HashHeap address is not instantiated!\n");
        }
        this.memory.put(address, newValue);
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        this.memory.forEach((a, v) -> str.append(a).append("->").append(v).append("\n"));
        return "HashHeap:\n" + str;
    }

    @Override
    public Map<Integer, Value> getContent() {
        return this.memory;
    }

    @Override
    public void setContent(Map<Integer, Value> newContent) {
        this.memory = newContent;
    }
}
