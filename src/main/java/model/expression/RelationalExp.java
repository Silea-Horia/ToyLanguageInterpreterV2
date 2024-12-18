package model.expression;

import model.adt.IDictionary;
import model.adt.IHeap;
import model.adt.ISymTable;
import model.exception.ExpressionException;
import model.type.BoolType;
import model.type.IType;
import model.type.IntType;
import model.value.BoolValue;
import model.value.IValue;
import model.value.IntValue;

public class RelationalExp implements IExp {
    private IExp left;
    private IExp right;
    private RelationalOperation relation;
    private IntType intType;

    public RelationalExp(IExp left, IExp right, RelationalOperation relation) {
        this.left = left;
        this.right = right;
        this.relation = relation;
        this.intType = new IntType();
    }

    @Override
    public IValue eval(ISymTable<String, IValue> tbl, IHeap heap) throws ExpressionException {
        try {
            IValue leftVal = left.eval(tbl, heap);

            if (!leftVal.getType().equals(this.intType)) {
                throw new ExpressionException("Left operand is not an Int type\n");
            }

            IValue rightVal = right.eval(tbl, heap);

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
        return new RelationalExp(left.deepCopy(), right.deepCopy(), relation);
    }

    @Override
    public IType typeCheck(IDictionary<String, IType> typeEnv) throws ExpressionException {
        if (this.left.typeCheck(typeEnv).equals(this.intType)) {
            if (this.right.typeCheck(typeEnv).equals(this.intType)) {
                return new BoolType();
            }
            throw new ExpressionException("Right operand is not an integer\n");
        }
        throw new ExpressionException("Left operand is not an integer\n");
    }

    @Override
    public String toString() {
        return switch (this.relation) {
            case SMALLER -> this.left + "<" + this.right;
            case SMALLEROREQUAL -> this.left + "<=" + this.right;
            case GREATER -> this.left + ">" + this.right;
            case GREATEROREQUAL -> this.left + ">=" + this.right;
            case EQUAL -> this.left + "==" + this.right;
            case NOTEQUAL -> this.left + "!=" + this.right;
            default -> this.left + "?" + this.right;
        };
    }
}
