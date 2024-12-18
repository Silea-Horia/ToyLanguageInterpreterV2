package model.statement;

import model.adt.IDictionary;
import model.exception.DictionaryException;
import model.exception.ExpressionException;
import model.exception.StmtException;
import model.expression.IExp;
import model.state.PrgState;
import model.type.IType;
import model.type.RefType;
import model.value.IValue;
import model.value.RefValue;

public class NewStmt implements IStmt {
    private String varName;
    private IExp exp;
    private RefType refType;

    public NewStmt(String varName, IExp exp) {
        this.varName = varName;
        this.exp = exp;
        //this.refType = new RefType(null);
    }

    @Override
    public PrgState execute(PrgState state) throws StmtException {
        try {
            IValue value = state.getSymTable().lookup(this.varName);
            IType type = value.getType();
            if (!value.getType().equals(type)) {
                throw new StmtException("Variable is not a ref type\n");
            }

            RefValue refValue = (RefValue) value;

            IValue res = this.exp.eval(state.getSymTable(), state.getHeap());

            if (!refValue.getLocationType().equals(res.getType())) {
                throw new StmtException("The expression type is different from the reference type😅\n");
            }

            int address = state.getHeap().allocate(res);

            state.getSymTable().insert(this.varName, new RefValue(address, refValue.getLocationType()));

            return null;
        } catch (DictionaryException | ExpressionException e) {
            throw new StmtException(e.getMessage());
        }

    }

    @Override
    public IStmt deepCopy() {
        return new NewStmt(this.varName, this.exp.deepCopy());
    }

    @Override
    public IDictionary<String, IType> typeCheck(IDictionary<String, IType> typeEnv) throws StmtException {
        try {
            IType varType = typeEnv.lookup(this.varName);
            IType expType = this.exp.typeCheck(typeEnv);
            if (varType.equals(new RefType(expType))) {
                return typeEnv;
            }
            throw new StmtException("RHS and LHS of NEW don't match types.\n");
        } catch (DictionaryException | ExpressionException e) {
            throw new StmtException(e.getMessage());
        }
    }

    @Override
    public String toString() {
        return "new(" + this.varName + ", " + this.exp + ')';
    }
}
