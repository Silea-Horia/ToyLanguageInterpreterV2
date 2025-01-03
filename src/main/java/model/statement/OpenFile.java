package model.statement;

import model.adt.IDictionary;
import model.exception.ExpressionException;
import model.exception.StmtException;
import model.expression.IExp;
import model.state.ProgramState;
import model.type.Type;
import model.type.StringType;
import model.value.IValue;
import model.value.StringValue;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class OpenFile implements Statement {
    private final IExp path;
    private static StringType stringType;

    public OpenFile(IExp path) {
        this.path = path;
        stringType = new StringType();
    }

    @Override
    public ProgramState execute(ProgramState state) throws StmtException {
        try {
            IValue result = this.path.eval(state.getSymTable(), state.getHeap());

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
    public Statement deepCopy() {
        return new OpenFile(path.deepCopy());
    }

    @Override
    public IDictionary<String, Type> typeCheck(IDictionary<String, Type> typeEnv) throws StmtException {
        try {
            if (this.path.typeCheck(typeEnv).equals(stringType)) {
                return typeEnv;
            }
            throw new StmtException("Expression is not a String.\n");
        } catch (ExpressionException e) {
            throw new StmtException(e.getMessage());
        }
    }

    @Override
    public String toString() {
        return "Open file: " + this.path;
    }
}
