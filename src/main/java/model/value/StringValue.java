package model.value;

import model.type.Type;
import model.type.StringType;

import java.util.Objects;

public class StringValue implements Value {
    private final String value;

    public StringValue(String value) {
        this.value = value;
    }

    @Override
    public Type getType() {
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
