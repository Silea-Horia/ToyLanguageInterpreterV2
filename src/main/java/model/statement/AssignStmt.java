package model.statement;

import model.adt.IDictionary;
import model.adt.ISymTable;
import model.adt.IExeStack;
import model.exception.DictionaryException;
import model.exception.ExpressionException;
import model.exception.StmtException;
import model.expression.IExp;
import model.state.PrgState;
import model.type.IType;
import model.value.IValue;

public class AssignStmt implements IStmt {
    private String id;
    private IExp exp;

    public AssignStmt(String id, IExp exp) {
        this.id = id;
        this.exp = exp;
    }

    @Override
    public String toString() {
        return this.id + "=" + this.exp;
    }

    @Override
    public PrgState execute(PrgState state) throws StmtException {
        IExeStack<IStmt> stack = state.getExeStack();
        ISymTable<String, IValue> tbl = state.getSymTable();
        IType typeId;
        if (tbl.contains(this.id)) {
            IValue val;
            try {
                val = this.exp.eval(tbl, state.getHeap());
            } catch (ExpressionException e) {
                throw new StmtException(e.getMessage());
            }
            try {
                typeId = (tbl.lookup(this.id)).getType();
                if (val.getType().equals(typeId)) {
                    tbl.insert(this.id, val);
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
    public IStmt deepCopy() {
        return new AssignStmt(this.id, this.exp.deepCopy());
    }

    @Override
    public IDictionary<String, IType> typeCheck(IDictionary<String, IType> typeEnv) throws StmtException {
        try {
            if (typeEnv.lookup(this.id).equals(this.exp.typeCheck(typeEnv))) {
                return typeEnv;
            }
            throw new StmtException("RHS and LHS have different types.\n");
        } catch (DictionaryException | ExpressionException e) {
            throw new StmtException(e.getMessage());
        }
    }
}
