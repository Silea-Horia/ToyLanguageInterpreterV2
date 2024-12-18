package model.value;

import model.type.BoolType;
import model.type.IType;

import java.util.Objects;

public class BoolValue implements IValue{
    private boolean value;

    public BoolValue(boolean val) { this.value = val; }

    public boolean getValue() { return this.value; }

    @Override
    public String toString() { return Boolean.toString(this.value); }

    @Override
    public IType getType() { return new BoolType(); }

    @Override
    public boolean equals(Object other) {
        return other instanceof BoolValue && Objects.equals(this.value, ((BoolValue) other).getValue());
    }
}
