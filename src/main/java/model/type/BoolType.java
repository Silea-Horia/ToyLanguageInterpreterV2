package model.type;

import model.value.BoolValue;
import model.value.IValue;

public class BoolType implements IType {
    @Override
    public boolean equals(Object other) {
        return other instanceof BoolType;
    }

    @Override
    public String toString() { return "bool"; }

    @Override
    public IValue getDefaultValue() {
        return new BoolValue(false);
    }
}
