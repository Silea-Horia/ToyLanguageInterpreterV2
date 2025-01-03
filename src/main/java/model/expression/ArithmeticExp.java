package model.expression;

import model.adt.Dictionary;
import model.adt.Heap;
import model.exception.ExpressionException;
import model.type.Type;
import model.type.IntType;
import model.value.Value;
import model.value.IntegerValue;

public class ArithmeticExp implements Expression {
    private final Expression leftOperand;
    private final Expression rightOperand;
    private final char operator;
    private final IntType intType;

    public ArithmeticExp(Expression leftOperand, Expression rightOperand, char operator) {
        this.leftOperand = leftOperand;
        this.rightOperand = rightOperand;
        this.operator = operator;
        this.intType = new IntType();
    }

    @Override
    public Value eval(Dictionary<String, Value> tbl, Heap heap) throws ExpressionException {
        Value v1, v2;
        v1 = this.leftOperand.eval(tbl, heap);
        if (v1.getType().equals(new IntType())) {
            v2 = this.rightOperand.eval(tbl, heap);
            if (v2.getType().equals(new IntType())) {
                int n1 = ((IntegerValue)v1).getValue();
                int n2 = ((IntegerValue)v2).getValue();
                switch (this.operator) {
                    case '+':
                        return new IntegerValue(n1 + n2);
                    case '-':
                        return new IntegerValue(n1 - n2);
                    case '*':
                        return new IntegerValue(n1 * n2);
                    case '/':
                        if (n2 == 0) throw new ExpressionException("Division by zero!\n");
                        else return new IntegerValue(n1 / n2);
                }
            } else throw new ExpressionException("Second operand is not an integer!\n");
        }
        throw new ExpressionException("First operand is not an integer!\n");
    }

    @Override
    public Expression deepCopy() {
        return new ArithmeticExp(this.leftOperand.deepCopy(), this.rightOperand.deepCopy(), this.operator);
    }

    @Override
    public Type typeCheck(Dictionary<String, Type> typeEnv) throws ExpressionException {
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
