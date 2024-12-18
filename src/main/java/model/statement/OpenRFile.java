package model.statement;

import model.adt.IDictionary;
import model.exception.ExpressionException;
import model.exception.StmtException;
import model.expression.IExp;
import model.state.PrgState;
import model.type.IType;
import model.type.StringType;
import model.value.IValue;
import model.value.StringValue;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class OpenRFile implements IStmt {
    private IExp exp;
    private static StringType stringType;

    public OpenRFile(IExp exp) {
        this.exp = exp;
        stringType = new StringType();
    }

    @Override
    public PrgState execute(PrgState state) throws StmtException {
        try {
            IValue result = this.exp.eval(state.getSymTable(), state.getHeap());

            if (!result.getType().equals(stringType)) {
                throw new StmtException("Expression is not a string\n");
            }

            String filePath = ((StringValue) result).getValue();

            if (state.getSymTable().contains(filePath)) {
                throw new StmtException("File is already open\n");
            }

            try {
                BufferedReader br = new BufferedReader(new FileReader(filePath));

                state.getFileTable().insert((StringValue)result, br);

                return null;
            } catch (FileNotFoundException e) {
                throw new StmtException("File not found\n");
            }
        } catch (ExpressionException e) {
            throw new StmtException(e.getMessage());
        }
    }

    @Override
    public IStmt deepCopy() {
        return new OpenRFile(exp.deepCopy());
    }

    @Override
    public IDictionary<String, IType> typeCheck(IDictionary<String, IType> typeEnv) throws StmtException {
        try {
            if (this.exp.typeCheck(typeEnv).equals(stringType)) {
                return typeEnv;
            }
            throw new StmtException("Expression is not a String.\n");
        } catch (ExpressionException e) {
            throw new StmtException(e.getMessage());
        }
    }

    @Override
    public String toString() {
        return "Open file: " + this.exp;
    }
}
