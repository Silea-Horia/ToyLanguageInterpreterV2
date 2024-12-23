package model.statement;

import model.adt.IDictionary;
import model.adt.ISymTable;
import model.exception.StmtException;
import model.state.ProgramState;
import model.type.IType;
import model.value.IValue;

public class VariableDeclaration implements Statement {
    private final IType type;
    private final String id;

    public VariableDeclaration(String id, IType type) {
        this.type = type;
        this.id = id;
    }

    @Override
    public String toString() {
        return this.type + " " + this.id;
    }

    @Override
    public ProgramState execute(ProgramState state) throws StmtException {
        ISymTable<String, IValue> symbols = state.getSymTable();
        if (symbols.contains(this.id)) throw new StmtException("Variable is already declared!\n");
        symbols.insert(this.id, this.type.getDefaultValue());
        return null;
    }

    @Override
    public Statement deepCopy() {
        return new VariableDeclaration(this.id, this.type);
    }

    @Override
    public IDictionary<String, IType> typeCheck(IDictionary<String, IType> typeEnv) throws StmtException {
        typeEnv.insert(this.id, this.type);
        return typeEnv;
    }
}
