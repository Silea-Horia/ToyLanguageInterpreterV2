package model.expression;

import model.adt.IDictionary;
import model.adt.IHeap;
import model.adt.ISymTable;
import model.exception.ExpressionException;
import model.type.Type;
import model.type.IntType;
import model.value.IValue;
import model.value.IntValue;

public class ArithExp implements IExp {
    private final IExp leftOperand;
    private final IExp rightOperand;
    private final char operator;
    private final IntType intType;

    public ArithExp(IExp leftOperand, IExp rightOperand, char operator) {
        this.leftOperand = leftOperand;
        this.rightOperand = rightOperand;
        this.operator = operator;
        this.intType = new IntType();
    }

    @Override
    public IValue eval(ISymTable<String, IValue> tbl, IHeap heap) throws ExpressionException {
        IValue v1, v2;
        v1 = this.leftOperand.eval(tbl, heap);
        if (v1.getType().equals(new IntType())) {
            v2 = this.rightOperand.eval(tbl, heap);
            if (v2.getType().equals(new IntType())) {
                int n1 = ((IntValue)v1).getValue();
                int n2 = ((IntValue)v2).getValue();
                switch (this.operator) {
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
        return new ArithExp(this.leftOperand.deepCopy(), this.rightOperand.deepCopy(), this.operator);
    }

    @Override
    public Type typeCheck(IDictionary<String, Type> typeEnv) throws ExpressionException {
        Type type1 = this.leftOperand.typeCheck(typeEnv);
        Type type2 = this.rightOperand.typeCheck(typeEnv);

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
        return switch (this.operator) {
            case '+' -> this.leftOperand + "+" + this.rightOperand;
            case '-' -> this.leftOperand + "-" + this.rightOperand;
            case '*' -> this.leftOperand + "*" + this.rightOperand;
            default -> this.leftOperand + "/" + this.rightOperand;
        };
    }
}
