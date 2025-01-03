package model.expression;

import model.adt.IDictionary;
import model.adt.IHeap;
import model.adt.ISymTable;
import model.exception.ExpressionException;
import model.type.Type;
import model.value.IValue;

public class ValueExp implements IExp{
    private final IValue value;

    public ValueExp(IValue value) { this.value = value; }

    @Override
    public IValue eval(ISymTable<String, IValue> tbl, IHeap heap) throws ExpressionException { return this.value; }

    @Override
    public IExp deepCopy() {
        return new ValueExp(this.value);
    }

    @Override
    public Type typeCheck(IDictionary<String, Type> typeEnv) throws ExpressionException {
        return this.value.getType();
    }

    @Override
    public String toString() {
        return this.value.toString();
    }
}
