package model.statement;

import model.adt.Dictionary;
import model.exception.DictionaryException;
import model.exception.ExpressionException;
import model.exception.StmtException;
import model.expression.Expression;
import model.state.ProgramState;
import model.type.Type;
import model.type.StringType;
import model.value.Value;
import model.value.StringValue;

import java.io.BufferedReader;
import java.io.IOException;

public class CloseFile implements Statement {
    private final Expression expression;
    private static StringType stringType;

    public CloseFile(Expression expression) {
        this.expression = expression;
        stringType = new StringType();
    }

    @Override
    public ProgramState execute(ProgramState state) throws StmtException {
        try {
            Value eval = this.expression.eval(state.getSymTable(), state.getHeap());

            try (BufferedReader br = state.getFileTable().lookup((StringValue) eval)){
                br.close();
                state.getFileTable().remove((StringValue) eval);
                return null;
            }
        } catch (ExpressionException | DictionaryException | IOException e) {
            throw new StmtException(e.getMessage());
        }
    }

    @Override
    public Statement deepCopy() {
        return new CloseFile(this.expression.deepCopy());
    }

    @Override
    public Dictionary<String, Type> typeCheck(Dictionary<String, Type> typeEnv) throws StmtException {
        try {
            if (this.expression.typeCheck(typeEnv).equals(stringType)) {
                return typeEnv;
            }
            throw new StmtException("Expression is not a String.\n");
        } catch (ExpressionException e) {
            throw new StmtException(e.getMessage());
        }
    }

    @Override
    public String toString() {
        return "Close file: " + this.expression;
    }
}
