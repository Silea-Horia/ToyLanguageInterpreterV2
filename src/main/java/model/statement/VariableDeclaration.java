package model.statement;

import model.adt.Dictionary;
import model.exception.StmtException;
import model.state.ProgramState;
import model.type.Type;
import model.value.Value;

public class VariableDeclaration implements Statement {
    private final Type type;
    private final String id;

    public VariableDeclaration(String id, Type type) {
        this.type = type;
        this.id = id;
    }

    @Override
    public String toString() {
        return this.type + " " + this.id;
    }

    @Override
    public ProgramState execute(ProgramState state) throws StmtException {
        Dictionary<String, Value> symbols = state.getSymTable();
        if (symbols.contains(this.id)) throw new StmtException("Variable is already declared!\n");
        symbols.insert(this.id, this.type.getDefaultValue());
        return null;
    }

    @Override
    public Statement deepCopy() {
        return new VariableDeclaration(this.id, this.type);
    }

    @Override
    public Dictionary<String, Type> typeCheck(Dictionary<String, Type> typeEnv) throws StmtException {
        typeEnv.insert(this.id, this.type);
        return typeEnv;
    }
}
