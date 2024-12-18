package model.value;

import model.type.IType;
import model.type.StringType;

import java.util.Objects;

public class StringValue implements IValue {
    private String value;

    public StringValue(String value) {
        this.value = value;
    }

    @Override
    public IType getType() {
        return new StringType();
    }

    public String getValue() {
        return this.value;
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof StringValue && Objects.equals(this.value, ((StringValue) other).getValue());
    }

    @Override
    public String toString() {
        return this.value;
    }
}
