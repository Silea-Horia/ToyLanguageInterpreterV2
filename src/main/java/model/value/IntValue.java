package model.value;

import model.type.Type;
import model.type.IntType;

import java.util.Objects;

public class IntValue implements IValue {
    private final int value;

    public IntValue(int val) { this.value = val; }

    public int getValue() { return value; }

    @Override
    public String toString() { return Integer.toString(this.value); }

    @Override
    public Type getType() { return new IntType(); }

    @Override
    public boolean equals(Object other) {
        return other instanceof IntValue && Objects.equals(this.value, ((IntValue) other).getValue());
    }
}
