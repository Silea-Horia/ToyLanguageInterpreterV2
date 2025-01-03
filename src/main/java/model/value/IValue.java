package model.value;

import model.type.Type;

public interface IValue {
    Type getType();
    boolean equals(Object other);
}
