package model.expression;

import model.adt.Dictionary;
import model.adt.Heap;
import model.exception.ExpressionException;
import model.type.BoolType;
import model.type.Type;
import model.value.BoolValue;
import model.value.Value;

public class LogicExp implements Expression {
    private final Expression leftOperand;
    private final Expression rightOperand;
    private final int operation; // 1 - and, 2 - or
    private final BoolType boolType;

    public LogicExp(Expression leftOperand, Expression rightOperand, int operation) {
        this.leftOperand = leftOperand;
        this.rightOperand = rightOperand;
        this.operation = operation;
        this.boolType = new BoolType();
    }

    @Override
    public Value eval(Dictionary<String, Value> tbl, Heap heap) throws ExpressionException {
        Value v1, v2;
        v1 = this.leftOperand.eval(tbl, heap);
        if (v1.getType().equals(this.boolType)) {
            v2 = this.rightOperand.eval(tbl, heap);
            if (v2.getType().equals(this.boolType)) {
                if (this.operation == 1) return new BoolValue(((BoolValue)v1).getValue() && ((BoolValue)v2).getValue());
                else return new BoolValue(((BoolValue)v1).getValue() || ((BoolValue)v2).getValue()  );
            } else throw new ExpressionException("Second operand is not boolean!\n");
        }
        throw new ExpressionException("First operand is not boolean!\n");
    }

    @Override
    public Expression deepCopy() {
        return new LogicExp(this.leftOperand.deepCopy(), this.rightOperand.deepCopy(), this.operation);
    }

    @Override
    public Type typeCheck(Dictionary<String, Type> typeEnv) throws ExpressionException {
        if (this.leftOperand.typeCheck(typeEnv).equals(this.boolType)) {
            if (this.rightOperand.typeCheck(typeEnv).equals(this.boolType)) {
                return new BoolType();
            }
            throw new ExpressionException("Right operand is not a boolean\n");
        }
        throw new ExpressionException("Left operand is not a boolean\n");
    }

    @Override
    public String toString() {
        String sign;

        if (this.operation == 1) sign = "&";
        else sign = "|";

        return this.leftOperand + sign + this.leftOperand;
    }
}
