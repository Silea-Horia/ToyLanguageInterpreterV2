package model.statement;

import model.adt.Dictionary;
import model.exception.ExpressionException;
import model.exception.StmtException;
import model.expression.Expression;
import model.state.ProgramState;
import model.type.BoolType;
import model.type.Type;
import model.value.BoolValue;
import model.value.Value;

public class While implements Statement {
    private final Expression condition;
    private final Statement command;
    private static BoolType boolType;

    public While(Expression condition, Statement command) {
        this.condition = condition;
        this.command = command;
        boolType = new BoolType();
    }

    @Override
    public ProgramState execute(ProgramState state) throws StmtException {
        try {
            Value eval = this.condition.eval(state.getSymTable(), state.getHeap());

            if (((BoolValue)eval).getValue()) {
                state.getExeStack().push(this);
                state.getExeStack().push(this.command);
            }

            return null;
        } catch (ExpressionException e) {
            throw new StmtException(e.getMessage());
        }
    }

    @Override
    public Statement deepCopy() {
        return new While(this.condition.deepCopy(), this.command.deepCopy());
    }

    @Override
    public Dictionary<String, Type> typeCheck(Dictionary<String, Type> typeEnv) throws StmtException {
        try {
            if (this.condition.typeCheck(typeEnv).equals(boolType)) {
                this.command.typeCheck(typeEnv);
                return typeEnv;
            }
            throw new StmtException("While condition is not a boolean.\n");
        } catch (ExpressionException e) {
            throw new StmtException(e.getMessage());
        }
    }

    @Override
    public String toString() {
        return "While(" + this.condition + ") " + this.command;
    }
}
