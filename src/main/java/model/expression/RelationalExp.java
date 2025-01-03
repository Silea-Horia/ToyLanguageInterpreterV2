package model.expression;

import model.adt.IDictionary;
import model.adt.IHeap;
import model.adt.ISymTable;
import model.exception.ExpressionException;
import model.type.BoolType;
import model.type.Type;
import model.type.IntType;
import model.value.BoolValue;
import model.value.IValue;
import model.value.IntValue;

public class RelationalExp implements IExp {
    private final IExp leftOperand;
    private final IExp rightOperand;
    private final RelationalOperation relation;
    private final IntType intType;

    public RelationalExp(IExp leftOperand, IExp rightOperand, RelationalOperation relation) {
        this.leftOperand = leftOperand;
        this.rightOperand = rightOperand;
        this.relation = relation;
        this.intType = new IntType();
    }

    @Override
    public IValue eval(ISymTable<String, IValue> tbl, IHeap heap) throws ExpressionException {
        try {
            IValue leftVal = leftOperand.eval(tbl, heap);

            if (!leftVal.getType().equals(this.intType)) {
                throw new ExpressionException("Left operand is not an Int type\n");
            }

            IValue rightVal = rightOperand.eval(tbl, heap);

            if (!rightVal.getType().equals(this.intType)) {
                throw new ExpressionException("Right operand is not an Int type\n");
            }

            switch (this.relation) {
                case SMALLER -> {
                    return new BoolValue(((IntValue)leftVal).getValue() < ((IntValue)rightVal).getValue());
                }

                case SMALLEROREQUAL -> {
                    return new BoolValue(((IntValue)leftVal).getValue() <= ((IntValue)rightVal).getValue());
                }

                case GREATER -> {
                    return new BoolValue(((IntValue)leftVal).getValue() > ((IntValue)rightVal).getValue());
                }

                case GREATEROREQUAL -> {
                    return new BoolValue(((IntValue)leftVal).getValue() >= ((IntValue)rightVal).getValue());
                }

                case EQUAL -> {
                    return new BoolValue(((IntValue)leftVal).getValue() == ((IntValue)rightVal).getValue());
                }

                case NOTEQUAL -> {
                    return new BoolValue(((IntValue)leftVal).getValue() != ((IntValue)rightVal).getValue());
                }

                default -> throw new ExpressionException("Invalid relation\n");
            }

        } catch (ExpressionException e) {
            throw new ExpressionException(e.getMessage());
        }
    }

    @Override
    public IExp deepCopy() {
        return new RelationalExp(leftOperand.deepCopy(), rightOperand.deepCopy(), relation);
    }

    @Override
    public Type typeCheck(IDictionary<String, Type> typeEnv) throws ExpressionException {
        if (this.leftOperand.typeCheck(typeEnv).equals(this.intType)) {
            if (this.rightOperand.typeCheck(typeEnv).equals(this.intType)) {
                return new BoolType();
            }
            throw new ExpressionException("Right operand is not an integer\n");
        }
        throw new ExpressionException("Left operand is not an integer\n");
    }

    @Override
    public String toString() {
        return switch (this.relation) {
            case SMALLER -> this.leftOperand + "<" + this.rightOperand;
            case SMALLEROREQUAL -> this.leftOperand + "<=" + this.rightOperand;
            case GREATER -> this.leftOperand + ">" + this.rightOperand;
            case GREATEROREQUAL -> this.leftOperand + ">=" + this.rightOperand;
            case EQUAL -> this.leftOperand + "==" + this.rightOperand;
            case NOTEQUAL -> this.leftOperand + "!=" + this.rightOperand;
            default -> this.leftOperand + "?" + this.rightOperand;
        };
    }
}
