package model.expression;

import model.adt.Dictionary;
import model.adt.Heap;
import model.exception.ExpressionException;
import model.type.Type;
import model.value.Value;

public interface Expression {
    Value eval(Dictionary<String, Value> tbl, Heap heap) throws ExpressionException;
    Expression deepCopy();
    Type typeCheck(Dictionary<String, Type> typeEnv) throws ExpressionException;
}
