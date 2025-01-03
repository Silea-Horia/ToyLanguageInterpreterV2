package model.statement;

import model.adt.Dictionary;
import model.exception.StmtException;
import model.state.ProgramState;
import model.type.Type;

public class Nop implements Statement {

    @Override
    public String toString() {
        return "NOP";
    }

    @Override
    public ProgramState execute(ProgramState state) throws StmtException {
        return null;
    }

    @Override
    public Statement deepCopy() {
        return new Nop();
    }

    @Override
    public Dictionary<String, Type> typeCheck(Dictionary<String, Type> typeEnv) throws StmtException {
        return typeEnv;
    }
}
