package model.statement;

import model.adt.Dictionary;
import model.adt.IExeStack;
import model.exception.ExpressionException;
import model.exception.StmtException;
import model.expression.Expression;
import model.state.ProgramState;
import model.type.BoolType;
import model.type.Type;
import model.value.BoolValue;
import model.value.Value;

public class If implements Statement {
    private final Expression expression;
    private final Statement thenBody;
    private final Statement elseBody;
    private static BoolType boolType;

    public If(Expression expression, Statement thenBody, Statement elseBody) {
        this.expression = expression;
        this.thenBody = thenBody;
        this.elseBody = elseBody;
        boolType = new BoolType();
    }

    @Override
    public String toString() {
        return "if(" + this.expression + ") thenBody(" + this.thenBody + ") else(" + this.elseBody + ")";
    }

    @Override
    public ProgramState execute(ProgramState state) throws StmtException {
        IExeStack<Statement> stack = state.getExeStack();
        Value condition;

        try {
            condition = this.expression.eval(state.getSymTable(), state.getHeap());
        } catch (ExpressionException e) {
            throw new StmtException(e.getMessage());
        }

        if (((BoolValue) condition).getValue()) stack.push(this.thenBody);
        else stack.push(this.elseBody);

        return null;
    }
    @Override
    public Statement deepCopy() {
        return new If(this.expression.deepCopy(), this.thenBody.deepCopy(), this.elseBody.deepCopy());
    }

    @Override
    public Dictionary<String, Type> typeCheck(Dictionary<String, Type> typeEnv) throws StmtException {
        try {
            if (this.expression.typeCheck(typeEnv).equals(boolType)) {
                this.thenBody.typeCheck(typeEnv.deepCopy());
                this.elseBody.typeCheck(typeEnv.deepCopy());
                return typeEnv;
            }
            throw new StmtException("The if condition is not a boolean.\n");
        } catch (ExpressionException e) {
            throw new StmtException(e.getMessage());
        }
    }
}
