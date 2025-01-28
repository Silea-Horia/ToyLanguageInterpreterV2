package model.statement;

import model.adt.Dictionary;
import model.exception.DictionaryException;
import model.exception.ExpressionException;
import model.exception.StmtException;
import model.expression.Expression;
import model.state.ProgramState;
import model.type.IntType;
import model.type.Type;
import model.value.IntegerValue;
import model.value.Value;

public class CreateSemaphore implements Statement {
    private final String variableId;
    private final Expression expression;

    public CreateSemaphore(String variableId, Expression expression) {
        this.variableId = variableId;
        this.expression = expression;
    }

    @Override
    public ProgramState execute(ProgramState state) throws StmtException {
        synchronized (state.getSemaphoreTable()) {
            try {
                Value result = this.expression.eval(state.getSymTable(), state.getHeap());

                int newFreeLocation = state.getSemaphoreTable().insert((IntegerValue) result);

                if (state.getSymTable().contains(this.variableId)) {
                    state.getSymTable().insert(this.variableId, new IntegerValue(newFreeLocation));
                    return null;
                }

                throw new StmtException("Semaphore cannot be assigned to a non existing variable!");

            } catch (ExpressionException e) {
                throw new StmtException(e.getMessage());
            }
        }
    }

    @Override
    public Statement deepCopy() {
        return new CreateSemaphore(this.variableId, this.expression.deepCopy());
    }

    @Override
    public Dictionary<String, Type> typeCheck(Dictionary<String, Type> typeEnv) throws StmtException {
        try {
            if (!this.expression.typeCheck(typeEnv).equals(new IntType())) {
                throw new StmtException("Expression is not of type int!");
            }

            if (typeEnv.lookup(this.variableId) instanceof IntType) {
                return typeEnv;
            }

            throw new StmtException("Variable is not of type int!");

        } catch (ExpressionException | DictionaryException e) {
            throw new StmtException(e.getMessage());
        }
    }

    @Override
    public String toString() {
        return "createSemaphore(" + this.variableId + ", " + this.expression + ")";
    }
}
