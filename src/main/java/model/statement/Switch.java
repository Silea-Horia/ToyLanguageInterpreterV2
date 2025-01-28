package model.statement;

import model.adt.Dictionary;
import model.exception.ExpressionException;
import model.exception.StmtException;
import model.expression.Expression;
import model.expression.RelationalExp;
import model.expression.RelationalOperation;
import model.state.ProgramState;
import model.type.IntType;
import model.type.Type;

public class Switch implements Statement {
    private final Expression condition;
    private final Expression match1;
    private final Statement block1;
    private final Expression match2;
    private final Statement block2;
    private final Statement defaultBlock;

    public Switch(Expression condition, Expression match1, Statement block1, Expression match2, Statement block2, Statement defaultBlock) {
        this.condition = condition;
        this.match1 = match1;
        this.block1 = block1;
        this.match2 = match2;
        this.block2 = block2;
        this.defaultBlock = defaultBlock;
    }

    @Override
    public ProgramState execute(ProgramState state) throws StmtException {
        Statement newIf = new If(new RelationalExp(this.condition, this.match1, RelationalOperation.EQUAL), this.block1,
                new If(new RelationalExp(this.condition, this.match2, RelationalOperation.EQUAL), this.block2, this.defaultBlock));
        state.getExeStack().push(newIf);
        return null;
    }

    @Override
    public Statement deepCopy() {
        return new Switch(this.condition.deepCopy(), this.match1.deepCopy(), this.block1.deepCopy(), this.match2.deepCopy(), this.block2.deepCopy(), this.defaultBlock.deepCopy());
    }

    @Override
    public Dictionary<String, Type> typeCheck(Dictionary<String, Type> typeEnv) throws StmtException {
        try {
            if (this.condition.typeCheck(typeEnv).equals(new IntType()) &&
                    this.match1.typeCheck(typeEnv).equals(new IntType()) &&
                    this.match2.typeCheck(typeEnv).equals(new IntType())) {

                this.block1.typeCheck(typeEnv);
                this.block2.typeCheck(typeEnv);
                this.defaultBlock.typeCheck(typeEnv);

                return typeEnv;
            }
            throw new StmtException("Conditions and matches aren't IntType!");
        } catch (ExpressionException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String toString() {
        return "switch("+ this.condition + ") (case " + match1 + ": " + block1 + ") (case " + match2 + ": " + block2 + ") (default: " + defaultBlock;
    }
}
