package model.value;

import model.type.Type;
import model.type.RefType;

import java.util.Objects;

public class RefValue implements IValue {
    private final int address;
    private final Type locationType;

    public RefValue(int address, Type locationType) {
        this.address = address;
        this.locationType = locationType;
    }

    public int getAddress() {
        return this.address;
    }

    public Type getLocationType() {
        return this.locationType;
    }

    @Override
    public Type getType() {
        return new RefType(this.locationType);
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof RefValue && Objects.equals(this.locationType, ((RefValue) other).locationType);
    }

    @Override
    public String toString() {
        return "(" + this.address + ", " + this.locationType + ")";
    }
}
