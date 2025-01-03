package model.type;

import model.value.IValue;
import model.value.RefValue;

public class RefType implements Type {
    private final Type inner;

    public RefType(Type inner) { this.inner = inner; }

    public Type getInner() { return inner; }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof RefType && ((RefType) obj).getInner().equals(this.inner);
    }

    @Override
    public String toString() {
        return "Ref (" + this.inner + ')';
    }

    @Override
    public IValue getDefaultValue() {
        return new RefValue(0, this.inner);
    }
}
