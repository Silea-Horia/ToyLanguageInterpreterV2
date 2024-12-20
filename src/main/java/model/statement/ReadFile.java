package model.statement;

import model.adt.IDictionary;
import model.adt.ISymTable;
import model.exception.DictionaryException;
import model.exception.ExpressionException;
import model.exception.StmtException;
import model.expression.IExp;
import model.state.ProgramState;
import model.type.IType;
import model.type.IntType;
import model.type.StringType;
import model.value.IValue;
import model.value.IntValue;
import model.value.StringValue;

import java.io.BufferedReader;
import java.io.IOException;

public class ReadFile implements Statement {
    private final IExp fileName;
    private final String id;
    private static IntType intType;
    private static StringType stringType;

    public ReadFile(IExp fileName, String id) {
        this.fileName = fileName;
        this.id = id;
        stringType = new StringType();
        intType = new IntType();
    }

    @Override
    public ProgramState execute(ProgramState state) throws StmtException {
        ISymTable<String, IValue> symTable = state.getSymTable();

        if (!symTable.contains(this.id)) {
            throw new StmtException("Variable doesn't exist");
        }

        try {
            if (!symTable.lookup(this.id).getType().equals(intType)) {
                throw new StmtException("Variable isn't type int");
            }

            IValue eval = this.fileName.eval(symTable, state.getHeap());

            try {
                BufferedReader br = state.getFileTable().lookup((StringValue) eval);
                String readVal = br.readLine();
                IntValue newValue;
                if (readVal == null) {
                    newValue = new IntValue(0);
                } else {
                    newValue = new IntValue(Integer.parseInt(readVal));
                }
                symTable.insert(this.id, newValue);
                return null;
            } catch (IOException e) {
                throw new StmtException(e.getMessage());
            }
        } catch (DictionaryException | ExpressionException e) {
            throw new StmtException(e.getMessage());
        }
    }

    @Override
    public Statement deepCopy() {
        return new ReadFile(this.fileName.deepCopy(), this.id);
    }

    @Override
    public IDictionary<String, IType> typeCheck(IDictionary<String, IType> typeEnv) throws StmtException {
        try {
            if (this.fileName.typeCheck(typeEnv).equals(stringType)) {
                return typeEnv;
            }
            throw new StmtException("Expression is not a String.\n");
        } catch (ExpressionException e) {
            throw new StmtException(e.getMessage());
        }
    }

    @Override
    public String toString() {
        return "Read from file: " + this.fileName;
    }
}
