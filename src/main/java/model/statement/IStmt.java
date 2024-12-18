package model.statement;

import model.adt.IDictionary;
import model.exception.StmtException;
import model.state.PrgState;
import model.type.IType;

public interface IStmt {
    PrgState execute(PrgState state) throws StmtException;
    IStmt deepCopy();
    IDictionary<String, IType> typeCheck(IDictionary<String, IType> typeEnv) throws StmtException;
}
