package model.type;

import model.value.Value;
import model.value.StringValue;

public class StringType implements Type {
    @Override
    public boolean equals(Object other) {
        return other instanceof StringType;
    }

    @Override
    public String toString() { return "string"; }

    @Override
    public Value getDefaultValue() {
        return new StringValue("");
    }
}
