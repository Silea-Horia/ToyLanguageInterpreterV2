package model.expression;

import model.adt.IDictionary;
import model.adt.IHeap;
import model.adt.ISymTable;
import model.exception.ExpressionException;
import model.type.IType;
import model.value.IValue;

public interface IExp {
    IValue eval(ISymTable<String, IValue> tbl, IHeap heap) throws ExpressionException;
    IExp deepCopy();
    IType typeCheck(IDictionary<String, IType> typeEnv) throws ExpressionException;
}
