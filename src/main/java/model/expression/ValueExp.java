package model.expression;

import model.adt.IDictionary;
import model.adt.IHeap;
import model.adt.ISymTable;
import model.exception.ExpressionException;
import model.type.IType;
import model.value.IValue;

public class ValueExp implements IExp{
    private IValue e;

    public ValueExp(IValue e) { this.e = e; }

    @Override
    public IValue eval(ISymTable<String, IValue> tbl, IHeap heap) throws ExpressionException { return this.e; }

    @Override
    public IExp deepCopy() {
        return new ValueExp(this.e);
    }

    @Override
    public IType typeCheck(IDictionary<String, IType> typeEnv) throws ExpressionException {
        return this.e.getType();
    }

    @Override
    public String toString() {
        return this.e.toString();
    }
}
