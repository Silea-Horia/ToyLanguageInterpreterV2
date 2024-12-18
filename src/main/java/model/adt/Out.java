package model.adt;

import java.util.ArrayList;

public class Out<T> implements IOutList<T> {
    private java.util.List<T> list;

    public Out() { this.list = new ArrayList<>(); }

    @Override
    public void add(T t) {
        this.list.add(t);
    }

    @Override
    public java.util.List<T> getAll() {
        return this.list;
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        this.list.forEach((t) -> str.append(t).append("\n"));
        return "Out:\n" + str;
    }
}
