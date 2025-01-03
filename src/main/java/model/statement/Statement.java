package model.statement;

import model.adt.IDictionary;
import model.exception.StmtException;
import model.state.ProgramState;
import model.type.Type;

public interface Statement {
    ProgramState execute(ProgramState state) throws StmtException;
    Statement deepCopy();
    IDictionary<String, Type> typeCheck(IDictionary<String, Type> typeEnv) throws StmtException;
}
