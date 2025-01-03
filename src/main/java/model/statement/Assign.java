package model.statement;

import model.adt.Dictionary;
import model.exception.DictionaryException;
import model.exception.ExpressionException;
import model.exception.StmtException;
import model.expression.Expression;
import model.state.ProgramState;
import model.type.Type;
import model.value.Value;

public class Assign implements Statement {
    private final String id;
    private final Expression expression;

    public Assign(String id, Expression expression) {
        this.id = id;
        this.expression = expression;
    }

    @Override
    public String toString() {
        return this.id + "=" + this.expression;
    }

    @Override
    public ProgramState execute(ProgramState state) throws StmtException {
        Dictionary<String, Value> symbols = state.getSymTable();

        if (symbols.contains(this.id)) {
            Value value;
            try {
                value = this.expression.eval(symbols, state.getHeap());
            } catch (ExpressionException e) {
                throw new StmtException(e.getMessage());
            }

            symbols.insert(this.id, value);
        } else {
            throw new StmtException("The used variable " + this.id + " was not declared before usage!\n");
        }
        return null;
    }

    @Override
    public Statement deepCopy() {
        return new Assign(this.id, this.expression.deepCopy());
    }

    @Override
    public Dictionary<String, Type> typeCheck(Dictionary<String, Type> typeEnv) throws StmtException {
        try {
            if (typeEnv.lookup(this.id).equals(this.expression.typeCheck(typeEnv))) {
                return typeEnv;
            }
            throw new StmtException("RHS and LHS have different types.\n");
        } catch (DictionaryException | ExpressionException e) {
            throw new StmtException(e.getMessage());
        }
    }
}
