package model.expression;

import model.adt.IDictionary;
import model.adt.IHeap;
import model.adt.ISymTable;
import model.exception.ExpressionException;
import model.type.IType;
import model.type.IntType;
import model.value.IValue;
import model.value.IntValue;

public class ArithExp implements IExp {
    private IExp e1;
    private IExp e2;
    private char op;
    private IntType intType;

    public ArithExp(IExp e1, IExp e2, char op) {
        this.e1 = e1;
        this.e2 = e2;
        this.op = op;
        this.intType = new IntType();
    }

    @Override
    public IValue eval(ISymTable<String, IValue> tbl, IHeap heap) throws ExpressionException {
        IValue v1, v2;
        v1 = this.e1.eval(tbl, heap);
        if (v1.getType().equals(new IntType())) {
            v2 = this.e2.eval(tbl, heap);
            if (v2.getType().equals(new IntType())) {
                int n1 = ((IntValue)v1).getValue();
                int n2 = ((IntValue)v2).getValue();
                switch (this.op) {
                    case '+':
                        return new IntValue(n1 + n2);
                    case '-':
                        return new IntValue(n1 - n2);
                    case '*':
                        return new IntValue(n1 * n2);
                    case '/':
                        if (n2 == 0) throw new ExpressionException("Division by zero!\n");
                        else return new IntValue(n1 / n2);
                }
            } else throw new ExpressionException("Second operand is not an integer!\n");
        }
        throw new ExpressionException("First operand is not an integer!\n");
    }

    @Override
    public IExp deepCopy() {
        return new ArithExp(this.e1.deepCopy(), this.e2.deepCopy(), this.op);
    }

    @Override
    public IType typeCheck(IDictionary<String, IType> typeEnv) throws ExpressionException {
        IType type1 = this.e1.typeCheck(typeEnv);
        IType type2 = this.e2.typeCheck(typeEnv);

        if (type1.equals(this.intType)) {
            if (type2.equals(this.intType)) {
                return new IntType();
            } else {
                throw new ExpressionException("Second operand is not an integer\n");
            }
        } else {
            throw new ExpressionException("Second operand is not an integer\n");
        }
    }

    @Override
    public String toString() {
        return switch (this.op) {
            case '+' -> this.e1 + "+" + this.e2;
            case '-' -> this.e1 + "-" + this.e2;
            case '*' -> this.e1 + "*" + this.e2;
            default -> this.e1 + "/" + this.e2;
        };
    }
}
