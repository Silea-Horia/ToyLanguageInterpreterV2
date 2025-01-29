package model.statement;

import model.adt.Dictionary;
import model.exception.ExpressionException;
import model.exception.StmtException;
import model.expression.Expression;
import model.expression.RelationalExp;
import model.expression.RelationalOperation;
import model.expression.VariableExp;
import model.state.ProgramState;
import model.type.IntType;
import model.type.Type;

public class For implements Statement {
    private final String variableId;
    private final Expression initialization;
    private final Expression condition;
    private final Expression step;
    private final Statement codeBlock;

    public For(String variableId, Expression initialization, Expression condition, Expression step, Statement codeBlock) {
        this.variableId = variableId;
        this.initialization = initialization;
        this.condition = condition;
        this.step = step;
        this.codeBlock = codeBlock;
    }

    @Override
    public ProgramState execute(ProgramState state) throws StmtException {
        Statement newStatement = new Composed(
                new VariableDeclaration(this.variableId, new IntType()),
                new Composed(
                        new Assign(this.variableId, this.initialization),
                        new While(new RelationalExp(new VariableExp(this.variableId), this.condition, RelationalOperation.SMALLER), new Composed(this.codeBlock, new Assign(this.variableId, this.step)))
                )
        );

        state.getExeStack().push(newStatement);

        return null;
    }

    @Override
    public Statement deepCopy() {
        return new For(this.variableId, this.initialization.deepCopy(), this.condition.deepCopy(), this.step.deepCopy(), this.codeBlock.deepCopy());
    }

    @Override
    public Dictionary<String, Type> typeCheck(Dictionary<String, Type> typeEnv) throws StmtException {
        try {
            typeEnv.insert(this.variableId, new IntType());

            if (!this.condition.typeCheck(typeEnv).equals(new IntType())) {
                throw new StmtException("For: Condition is not an int type!");
            }

            if (!this.step.typeCheck(typeEnv).equals(new IntType())) {
                throw new StmtException("For: Step is not an int type!");
            }
        } catch (ExpressionException e) {
            throw new StmtException(e.getMessage());
        }

        return typeEnv;
    }

    @Override
    public String toString() {
        return "For(" + this.variableId + "=" + this.initialization + "; " + this.variableId + "<" + this.condition + "; " + this.variableId + "=" + this.step + ") " + this.codeBlock;
    }
}
