package model.type;

import model.value.IValue;
import model.value.StringValue;

public class StringType implements IType {
    @Override
    public boolean equals(Object other) {
        return other instanceof StringType;
    }

    @Override
    public String toString() { return "string"; }

    @Override
    public IValue getDefaultValue() {
        return new StringValue("");
    }
}
