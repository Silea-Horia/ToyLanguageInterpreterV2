package model.adt;

import javafx.util.Pair;
import model.exception.DictionaryException;
import model.value.IntegerValue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SemaphoreTableImpl implements SemaphoreTable {
    private final Map<Integer, Pair<Integer, List<Integer>>> table;
    private int currentFree;

    public SemaphoreTableImpl() {
        this.table = new HashMap<>();
        this.currentFree = 0;
    }

    private void getNextFree() {
        int nextFree = 0;
        while (nextFree < this.currentFree && this.table.containsKey(nextFree)) nextFree++;
        this.currentFree = nextFree;
    }

    @Override
    synchronized public int insert(IntegerValue number) {
        this.getNextFree();
        this.table.put(this.currentFree, new Pair<>(number.getValue(), new ArrayList<>()));
        return this.currentFree;
    }

    @Override
    synchronized public Pair<Integer, List<Integer>> lookup(Integer key) throws DictionaryException {
        return this.table.get(key);
    }

    @Override
    synchronized public Map<Integer, Pair<Integer, List<Integer>>> getContent() {
        return this.table;
    }
}
