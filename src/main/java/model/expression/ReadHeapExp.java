package model.expression;

import model.adt.Dictionary;
import model.adt.Heap;
import model.exception.DictionaryException;
import model.exception.ExpressionException;
import model.type.Type;
import model.type.RefType;
import model.value.Value;
import model.value.ReferenceValue;

public class ReadHeapExp implements Expression {
    private final Expression expression;

    public ReadHeapExp(Expression expression) {
        this.expression = expression;
    }

    @Override
    public Value eval(Dictionary<String, Value> tbl, Heap heap) throws ExpressionException {
        Value res = this.expression.eval(tbl, heap);

        if (!(res.getType() instanceof  RefType)) {
            throw new ExpressionException("Expression is not a ref type\n");
        }

        ReferenceValue refValue = (ReferenceValue) res;

        Integer key = refValue.getAddress();

        try {
            return heap.getValue(key);
        } catch (DictionaryException e) {
            throw new ExpressionException(e.getMessage());
        }
    }

    @Override
    public Expression deepCopy() {
        return new ReadHeapExp(this.expression.deepCopy());
    }

    @Override
    public Type typeCheck(Dictionary<String, Type> typeEnv) throws ExpressionException {
        Type type;
        if ((type = this.expression.typeCheck(typeEnv)) instanceof RefType) {
            return ((RefType)type).getInner();
        }
        throw new ExpressionException("The Read HashHeap argument is not a Ref Type.\n");
    }

    @Override
    public String toString() {
        return "rH(" + this.expression + ')';
    }
}
