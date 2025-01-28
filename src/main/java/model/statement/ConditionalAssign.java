package model.statement;

import model.adt.Dictionary;
import model.exception.DictionaryException;
import model.exception.ExpressionException;
import model.exception.StmtException;
import model.expression.Expression;
import model.state.ProgramState;
import model.type.BoolType;
import model.type.Type;

public class ConditionalAssign implements Statement {
    private final String id;
    private final Expression condition;
    private final Expression thenBlock;
    private final Expression elseBlock;

    public ConditionalAssign(String id, Expression condition, Expression thenBlock, Expression elseBlock) {
        this.id = id;
        this.condition = condition;
        this.thenBlock = thenBlock;
        this.elseBlock = elseBlock;
    }

    @Override
    public ProgramState execute(ProgramState state) throws StmtException {
        state.getExeStack().push(new If(this.condition, new Assign(this.id, this.thenBlock), new Assign(this.id, this.elseBlock)));
        return null;
    }

    @Override
    public Statement deepCopy() {
        return new ConditionalAssign(this.id, this.condition.deepCopy(), this.thenBlock.deepCopy(), this.elseBlock.deepCopy());
    }

    @Override
    public Dictionary<String, Type> typeCheck(Dictionary<String, Type> typeEnv) throws StmtException {
        try {
            if (typeEnv.lookup(this.id).equals(this.thenBlock.typeCheck(typeEnv)) && typeEnv.lookup(this.id).equals(this.elseBlock.typeCheck(typeEnv))) {
                if (!this.condition.typeCheck(typeEnv).equals(new BoolType())) {
                    throw new StmtException("Condition is not of type Bool!");
                }
                return typeEnv;
            }
            throw new StmtException("Variable and assigned value don't have he same type");
        } catch (ExpressionException | DictionaryException e) {
            throw new StmtException(e.getMessage());
        }

    }

    @Override
    public String toString() {
        return this.id + "=" + this.condition + "?" + this.thenBlock + ":" + this.elseBlock;
    }
}
