package model.adt;

import java.util.List;

public interface IOutList<T> {
    void add(T t);
    List<T> getAll();
}
