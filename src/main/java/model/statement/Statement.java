package model.statement;

import model.adt.Dictionary;
import model.exception.StmtException;
import model.state.ProgramState;
import model.type.Type;

public interface Statement {
    ProgramState execute(ProgramState state) throws StmtException;
    Statement deepCopy();
    Dictionary<String, Type> typeCheck(Dictionary<String, Type> typeEnv) throws StmtException;
}
