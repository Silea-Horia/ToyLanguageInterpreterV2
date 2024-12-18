package model.statement;

import model.adt.IDictionary;
import model.adt.ISymTable;
import model.exception.StmtException;
import model.state.PrgState;
import model.type.IType;
import model.value.IValue;

public class VarDeclStmt implements IStmt {
    private IType type;
    private String id;

    public VarDeclStmt(String id, IType type) {
        this.type = type;
        this.id = id;
    }

    @Override
    public String toString() {
        return this.type + " " + this.id;
    }

    @Override
    public PrgState execute(PrgState state) throws StmtException {
        ISymTable<String, IValue> tbl = state.getSymTable();
        if (tbl.contains(this.id)) throw new StmtException("Variable is already declared!\n");
        tbl.insert(this.id, this.type.getDefaultValue());
        return null;
    }

    @Override
    public IStmt deepCopy() {
        return new VarDeclStmt(this.id, this.type);
    }

    @Override
    public IDictionary<String, IType> typeCheck(IDictionary<String, IType> typeEnv) throws StmtException {
        typeEnv.insert(this.id, this.type);
        return typeEnv;
    }
}
