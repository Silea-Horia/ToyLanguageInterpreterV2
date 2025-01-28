package model.adt;

import javafx.util.Pair;
import model.exception.DictionaryException;
import model.value.IntegerValue;

import java.util.List;
import java.util.Map;

public interface SemaphoreTable {
    int insert(IntegerValue number);
    Pair<Integer, List<Integer>> lookup(Integer key) throws DictionaryException;
    Map<Integer, Pair<Integer, List<Integer>>> getContent();
}
