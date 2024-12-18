package model.expression;

import model.adt.IDictionary;
import model.adt.IHeap;
import model.adt.ISymTable;
import model.exception.DictionaryException;
import model.exception.ExpressionException;
import model.type.IType;
import model.value.IValue;

public class VarExp implements IExp {
    private String id;

    public VarExp(String id) { this.id = id; }

    @Override
    public IValue eval(ISymTable<String, IValue> tbl, IHeap heap) throws ExpressionException {
        try {
            return tbl.lookup(this.id);
        } catch (DictionaryException ex) {
            throw new ExpressionException("Variable not defined!\n");
        }
    }

    @Override
    public IExp deepCopy() {
        return new VarExp(this.id);
    }

    @Override
    public IType typeCheck(IDictionary<String, IType> typeEnv) throws ExpressionException {
        try {
            return typeEnv.lookup(this.id);
        } catch (DictionaryException e) {
            throw new ExpressionException(e.getMessage());
        }
    }

    @Override
    public String toString() {
        return this.id;
    }
}
