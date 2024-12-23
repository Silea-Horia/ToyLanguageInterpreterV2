package model.statement;

import model.adt.IDictionary;
import model.adt.ISymTable;
import model.exception.DictionaryException;
import model.exception.ExpressionException;
import model.exception.StmtException;
import model.expression.IExp;
import model.state.ProgramState;
import model.type.IType;
import model.value.IValue;

public class Assign implements Statement {
    private final String id;
    private final IExp expression;

    public Assign(String id, IExp expression) {
        this.id = id;
        this.expression = expression;
    }

    @Override
    public String toString() {
        return this.id + "=" + this.expression;
    }

    @Override
    public ProgramState execute(ProgramState state) throws StmtException {
        ISymTable<String, IValue> symbols = state.getSymTable();
        if (symbols.contains(this.id)) {
            IValue value;
            try {
                value = this.expression.eval(symbols, state.getHeap());
            } catch (ExpressionException e) {
                throw new StmtException(e.getMessage());
            }
            try {
                IType typeId = (symbols.lookup(this.id)).getType();
                if (value.getType().equals(typeId)) {
                    symbols.insert(this.id, value);
                } else {
                    throw new StmtException("Declared type of variable " + this.id + " doesn't match the type of the assigned expression!\n");
                }
            } catch (DictionaryException _) {
                throw new StmtException("WTF?!\n");
            }
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
    public IDictionary<String, IType> typeCheck(IDictionary<String, IType> typeEnv) throws StmtException {
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
