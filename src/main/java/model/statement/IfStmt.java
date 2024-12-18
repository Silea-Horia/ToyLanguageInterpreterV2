package model.statement;

import model.adt.IDictionary;
import model.adt.IExeStack;
import model.exception.ExpressionException;
import model.exception.StmtException;
import model.expression.IExp;
import model.state.PrgState;
import model.type.BoolType;
import model.type.IType;
import model.value.BoolValue;
import model.value.IValue;

public class IfStmt implements IStmt {
    private IExp exp;
    private IStmt thenS;
    private IStmt elseS;
    private static BoolType boolType;

    public IfStmt(IExp exp, IStmt thenS, IStmt elseS) {
        this.exp = exp;
        this.thenS = thenS;
        this.elseS = elseS;
        boolType = new BoolType();
    }

    @Override
    public String toString() {
        return "IF(" + this.exp + ") THEN(" + this.thenS + ") ELSE(" + this.elseS + ")";
    }

    @Override
    public PrgState execute(PrgState state) throws StmtException {
        IExeStack<IStmt> stack = state.getExeStack();
        IValue cond;
        try {
            cond = this.exp.eval(state.getSymTable(), state.getHeap());
        } catch (ExpressionException e) {
            throw new StmtException(e.getMessage());
        }
        if (!cond.getType().equals(boolType)) throw new StmtException("Conditional expression is not a boolean!|n");
        if (((BoolValue)cond).getValue()) stack.push(this.thenS);
        else stack.push(this.elseS);
        return null;
    }
    @Override
    public IStmt deepCopy() {
        return new IfStmt(this.exp.deepCopy(), this.thenS.deepCopy(), this.elseS.deepCopy());
    }

    @Override
    public IDictionary<String, IType> typeCheck(IDictionary<String, IType> typeEnv) throws StmtException {
        try {
            if (this.exp.typeCheck(typeEnv).equals(boolType)) {
                this.thenS.typeCheck(typeEnv.deepCopy());
                this.elseS.typeCheck(typeEnv.deepCopy());
                return typeEnv;
            }
            throw new StmtException("The if condition is not a boolean.\n");
        } catch (ExpressionException e) {
            throw new StmtException(e.getMessage());
        }
    }
}
