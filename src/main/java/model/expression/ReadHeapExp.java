package model.expression;

import model.adt.IDictionary;
import model.adt.IHeap;
import model.adt.ISymTable;
import model.exception.DictionaryException;
import model.exception.ExpressionException;
import model.type.IType;
import model.type.RefType;
import model.value.IValue;
import model.value.RefValue;

public class ReadHeapExp implements IExp {
    private IExp exp;

    public ReadHeapExp(IExp exp) {
        this.exp = exp;
    }

    @Override
    public IValue eval(ISymTable<String, IValue> tbl, IHeap heap) throws ExpressionException {
        IValue res = this.exp.eval(tbl, heap);

        if (!(res.getType() instanceof  RefType)) {
            throw new ExpressionException("Expression is not a ref type\n");
        }

        RefValue refValue = (RefValue) res;

        Integer key = refValue.getAddress();

        try {
            return heap.getValue(key);
        } catch (DictionaryException e) {
            throw new ExpressionException(e.getMessage());
        }
    }

    @Override
    public IExp deepCopy() {
        return new ReadHeapExp(this.exp.deepCopy());
    }

    @Override
    public IType typeCheck(IDictionary<String, IType> typeEnv) throws ExpressionException {
        IType type;
        if ((type = this.exp.typeCheck(typeEnv)) instanceof RefType) {
            return ((RefType)type).getInner();
        }
        throw new ExpressionException("The Read Heap argument is not a Ref Type.\n");
    }

    @Override
    public String toString() {
        return "rH(" + this.exp + ')';
    }
}
