package model.statement;

import model.adt.IDictionary;
import model.exception.StmtException;
import model.state.PrgState;
import model.type.IType;

public class NopStmt implements IStmt {

    @Override
    public String toString() {
        return "NOP";
    }

    @Override
    public PrgState execute(PrgState state) throws StmtException {
        return null;
    }

    @Override
    public IStmt deepCopy() {
        return new NopStmt();
    }

    @Override
    public IDictionary<String, IType> typeCheck(IDictionary<String, IType> typeEnv) throws StmtException {
        return typeEnv;
    }
}
