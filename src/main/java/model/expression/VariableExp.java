package model.expression;

import model.adt.Dictionary;
import model.adt.Heap;
import model.exception.DictionaryException;
import model.exception.ExpressionException;
import model.type.Type;
import model.value.Value;

public class VariableExp implements Expression {
    private final String id;

    public VariableExp(String id) { this.id = id; }

    @Override
    public Value eval(Dictionary<String, Value> tbl, Heap heap) throws ExpressionException {
        try {
            return tbl.lookup(this.id);
        } catch (DictionaryException ex) {
            throw new ExpressionException("Variable not defined!\n");
        }
    }

    @Override
    public Expression deepCopy() {
        return new VariableExp(this.id);
    }

    @Override
    public Type typeCheck(Dictionary<String, Type> typeEnv) throws ExpressionException {
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
