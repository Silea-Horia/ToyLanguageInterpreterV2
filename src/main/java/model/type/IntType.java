package model.type;

import model.value.Value;
import model.value.IntegerValue;

public class IntType implements Type {
    @Override
    public boolean equals(Object other) {
        return other instanceof IntType;
    }

    @Override
    public String toString() { return "int"; }

    @Override
    public Value getDefaultValue() {
        return new IntegerValue(0);
    }
}
