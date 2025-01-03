package repository;

import model.adt.*;
import model.exception.RepoException;
import model.exception.StmtException;
import model.expression.*;
import model.state.ProgramState;
import model.statement.*;
import model.type.BoolType;
import model.type.IntType;
import model.type.RefType;
import model.type.StringType;
import model.value.BoolValue;
import model.value.IntegerValue;
import model.value.StringValue;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static model.expression.RelationalOperation.GREATER;

public class MemoryRepository implements Repository {
    private List<ProgramState> programs;
    private final String logFilePath;
    private final ArrayList<Statement> generatedStatements;

    public MemoryRepository(String logFilePath) {
        this.programs = new ArrayList<>();
        this.logFilePath = logFilePath;
        this.generatedStatements = new ArrayList<>();
        this.generateStates();

        try {
            PrintWriter writer = new PrintWriter(logFilePath);
            writer.print("");
            writer.close();
        } catch (FileNotFoundException _) {}
    }

    @Override
    public void addPrgState(ProgramState state) {
        this.programs.add(state);
    }

    @Override
    public void logPrgState(ProgramState state) throws RepoException {
        try {
            PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(this.logFilePath, true)));
            pw.println(state);
            pw.close();
        } catch (IOException ex) {
            throw new RepoException(ex.getMessage());
        }
    }

    @Override
    public List<ProgramState> getPrgList() {
        return this.programs;
    }

    @Override
    public void setPrgList(List<ProgramState> newList) {
        this.programs = newList;
    }

    @Override
    public ArrayList<Statement> getGeneratedStatements() {
        return this.generatedStatements;
    }

    @Override
    public int getNoPrograms() {
        return this.programs.size();
    }

    private void generateState1() {
        this.generatedStatements.add(new Composed(new VariableDeclaration("v", new IntType()),
                            new Composed(new Assign("v",new ValueExp(new IntegerValue(2))), new Print(new VariableExp("v")))));
    }

    private void generateState2() {
        this.generatedStatements.add(new Composed(new VariableDeclaration("a",new IntType()),
                new Composed(new VariableDeclaration("b",new IntType()),
                        new Composed(new Assign("a", new ArithmeticExp(new ValueExp(new IntegerValue(2)),new ArithmeticExp(new ValueExp(new IntegerValue(3)), new ValueExp(new IntegerValue(5)), '*'), '+')),
                                new Composed(new Assign("b",new ArithmeticExp(new VariableExp("a"), new ValueExp(new IntegerValue(1)), '+')),
                                        new Print(new VariableExp("b")))))));
    }

    private void generateState3() {
        this.generatedStatements.add(new Composed(new VariableDeclaration("a",new BoolType()),
                new Composed(new VariableDeclaration("v", new IntType()),
                        new Composed(new Assign("a", new ValueExp(new BoolValue(true))),
                                new Composed(new If(new VariableExp("a"),new Assign("v",new ValueExp(new
                                        IntegerValue(2))), new Assign("v", new ValueExp(new IntegerValue(3)))), new Print(new VariableExp("v")))))));
    }

    private void generateState4() {
        this.generatedStatements.add(new Composed(new VariableDeclaration("varf",new StringType()),
                new Composed(new Assign("varf", new ValueExp(new StringValue("src/main/java/files/test.in"))),
                        new Composed(new OpenFile(new VariableExp("varf")),
                                new Composed(new VariableDeclaration("varc", new IntType()),
                                        new Composed(new ReadFile(new VariableExp("varf"), "varc"),
                                                new Composed(new Print(new VariableExp("varc")),
                                                        new Composed(new ReadFile(new VariableExp("varf"), "varc"),
                                                                new Composed(new Print(new VariableExp("varc")),
                                                                        new CloseFile(new VariableExp("varf")))))))))));
    }

    private void generateState5() {
        this.generatedStatements.add(new Composed(new VariableDeclaration("v", new RefType(new IntType())),
                new Composed(new New("v", new ValueExp(new IntegerValue(20))),
                        new Composed(new VariableDeclaration("a", new RefType(new RefType(new IntType()))),
                                new Composed(new New("a", new VariableExp("v")),
                                        new Composed(new Print(new ReadHeapExp(new VariableExp("v"))),
                                                new Composed(new Print(new ArithmeticExp(new ReadHeapExp(new ReadHeapExp(new VariableExp("a"))), new ValueExp(new IntegerValue(5)), '+')),
                                                        new Composed(new Print(new ReadHeapExp(new VariableExp("v"))),
                                                                new Composed(new WriteHeap("v", new ValueExp(new IntegerValue(30))),
                                                                        new Print(new ReadHeapExp(new VariableExp("v"))))))))))));
    }

    private void generateState6() {
        this.generatedStatements.add(new Composed(new VariableDeclaration("v", new RefType(new IntType())),
                new Composed(new New("v", new ValueExp(new IntegerValue(20))),
                        new Composed(new VariableDeclaration("a", new RefType(new RefType(new IntType()))),
                                new Composed(new New("a", new VariableExp("v")),
                                        new Composed(new New("v", new ValueExp(new IntegerValue(30))),
                                                new Composed(new Print(new ReadHeapExp(new ReadHeapExp(new VariableExp("a")))),
                                                        new New("v", new ValueExp(new IntegerValue(50))))))))));
    }

    private void generateState7() {
        this.generatedStatements.add(new Composed(new VariableDeclaration("v", new IntType()),
                new Composed(new Assign("v", new ValueExp(new IntegerValue(4))),
                        new Composed(new While(new RelationalExp(new VariableExp("v"), new ValueExp(new IntegerValue(0)), GREATER),
                                new Assign("v", new ArithmeticExp(new VariableExp("v"), new ValueExp(new IntegerValue(1)), '-'))),
                                new Print(new VariableExp("v"))))));
    }

    private void generateState8() {
        this.generatedStatements.add(new Composed(
          new VariableDeclaration("v", new IntType()), new Composed(
                  new VariableDeclaration("a", new RefType(new IntType())), new Composed(
                          new Assign("v", new ValueExp(new IntegerValue(10))), new Composed(
                                  new New("a", new ValueExp(new IntegerValue(22))), new Composed(
                                          new Fork(new Composed(
                                                  new WriteHeap("a", new ValueExp(new IntegerValue(30))), new Composed(
                                                          new Assign("v", new ValueExp(new IntegerValue(32))), new Composed(
                                                                  new Print(new VariableExp("v")),
                                                                    new Print(new ReadHeapExp(new VariableExp("a")))
                                          )
                                          )
                                          )
                                          ), new Composed(
                                                  new Print(new VariableExp("v")), new Print(new ReadHeapExp(new VariableExp("a")))
                                            )
                                )
                        )
                )
            )
        ));
    }

    private void generateState9() {
        // Ref inv v; new(v, 20); Ref Ref int a; new(a, v); fork(Ref int b; new(v,30); new(b,v););
        this.generatedStatements.add(new Composed(
          new VariableDeclaration("v", new RefType(new IntType())), new Composed(
                  new New("v", new ValueExp(new IntegerValue(20))), new Composed(
                          new VariableDeclaration("a", new RefType(new RefType(new IntType()))), new Composed(
                                  new New("a", new VariableExp("v")), new Composed(
                                      new Fork(
                                              new Composed(
                                                      new VariableDeclaration("b", new RefType(new RefType(new IntType()))), new Composed(
                                                        new New("v", new ValueExp(new IntegerValue(30))),
                                                        new New("b", new VariableExp("v"))
                                              )
                                              )
                                      ), new Composed(
                                              new Print(new ReadHeapExp(new VariableExp("v"))), new Print(new ReadHeapExp(new ReadHeapExp(new VariableExp("a"))))
        )
        )
        )
        )
        )
        ));
    }

    private void generateStates() {
        this.generateState1();
        this.generateState2();
        this.generateState3();
        this.generateState4();
        this.generateState5();
        this.generateState6();
        this.generateState7();
        this.generateState8();
        this.generateState9();
    }

    @Override
    public void setState(int option) throws RepoException{
        Statement initialStatement = this.generatedStatements.get(option);
        try {
            initialStatement.typeCheck(new HashDictionary<>());
        } catch (StmtException e) {
            throw new RepoException(e.getMessage());
        }
        this.programs.clear();
        this.programs.add(new ProgramState(new ExeStack<>(), new SymTable<>(), new Out<>(), initialStatement, new FileTable<>(), new HashHeap(), 0));
    }
}
