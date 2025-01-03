package model.type;

import model.value.Value;

public interface Type {
    boolean equals(Object other);
    Value getDefaultValue();
}
