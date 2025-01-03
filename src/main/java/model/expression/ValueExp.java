package model.expression;

import model.adt.Dictionary;
import model.adt.Heap;
import model.exception.ExpressionException;
import model.type.Type;
import model.value.Value;

public class ValueExp implements Expression {
    private final Value value;

    public ValueExp(Value value) { this.value = value; }

    @Override
    public Value eval(Dictionary<String, Value> tbl, Heap heap) throws ExpressionException { return this.value; }

    @Override
    public Expression deepCopy() {
        return new ValueExp(this.value);
    }

    @Override
    public Type typeCheck(Dictionary<String, Type> typeEnv) throws ExpressionException {
        return this.value.getType();
    }

    @Override
    public String toString() {
        return this.value.toString();
    }
}
