package model.value;

import model.type.IType;
import model.type.RefType;

import java.util.Objects;

public class RefValue implements IValue {
    private int address;
    private IType locationType;

    public RefValue(int address, IType locationType) {
        this.address = address;
        this.locationType = locationType;
    }

    public int getAddress() {
        return this.address;
    }

    public IType getLocationType() {
        return this.locationType;
    }

    @Override
    public IType getType() {
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
