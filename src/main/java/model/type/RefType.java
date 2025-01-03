package model.type;

import model.value.Value;
import model.value.ReferenceValue;

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
    public Value getDefaultValue() {
        return new ReferenceValue(0, this.inner);
    }
}
