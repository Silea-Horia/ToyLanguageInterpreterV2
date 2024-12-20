package model.statement;

import model.adt.IDictionary;
import model.exception.StmtException;
import model.state.ProgramState;
import model.type.IType;

public interface Statement {
    ProgramState execute(ProgramState state) throws StmtException;
    Statement deepCopy();
    IDictionary<String, IType> typeCheck(IDictionary<String, IType> typeEnv) throws StmtException;
}
