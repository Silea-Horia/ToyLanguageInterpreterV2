package model.value;

import model.type.Type;
import model.type.RefType;

import java.util.Objects;

public class ReferenceValue implements Value {
    private final int address;
    private final Type locationType;

    public ReferenceValue(int address, Type locationType) {
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
        return other instanceof ReferenceValue && Objects.equals(this.locationType, ((ReferenceValue) other).locationType);
    }

    @Override
    public String toString() {
        return "(" + this.address + ", " + this.locationType + ")";
    }
}
