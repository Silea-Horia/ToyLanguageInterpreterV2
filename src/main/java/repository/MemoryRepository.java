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
import model.value.IntValue;
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
                            new Composed(new Assign("v",new ValueExp(new IntValue(2))), new Print(new VarExp("v")))));
    }

    private void generateState2() {
        this.generatedStatements.add(new Composed(new VariableDeclaration("a",new IntType()),
                new Composed(new VariableDeclaration("b",new IntType()),
                        new Composed(new Assign("a", new ArithExp(new ValueExp(new IntValue(2)),new ArithExp(new ValueExp(new IntValue(3)), new ValueExp(new IntValue(5)), '*'), '+')),
                                new Composed(new Assign("b",new ArithExp(new VarExp("a"), new ValueExp(new IntValue(1)), '+')),
                                        new Print(new VarExp("b")))))));
    }

    private void generateState3() {
        this.generatedStatements.add(new Composed(new VariableDeclaration("a",new BoolType()),
                new Composed(new VariableDeclaration("v", new IntType()),
                        new Composed(new Assign("a", new ValueExp(new BoolValue(true))),
                                new Composed(new If(new VarExp("a"),new Assign("v",new ValueExp(new
                                        IntValue(2))), new Assign("v", new ValueExp(new IntValue(3)))), new Print(new VarExp("v")))))));
    }

    private void generateState4() {
        this.generatedStatements.add(new Composed(new VariableDeclaration("varf",new StringType()),
                new Composed(new Assign("varf", new ValueExp(new StringValue("src/main/java/files/test.in"))),
                        new Composed(new OpenFile(new VarExp("varf")),
                                new Composed(new VariableDeclaration("varc", new IntType()),
                                        new Composed(new ReadFile(new VarExp("varf"), "varc"),
                                                new Composed(new Print(new VarExp("varc")),
                                                        new Composed(new ReadFile(new VarExp("varf"), "varc"),
                                                                new Composed(new Print(new VarExp("varc")),
                                                                        new CloseFile(new VarExp("varf")))))))))));
    }

    private void generateState5() {
        this.generatedStatements.add(new Composed(new VariableDeclaration("v", new RefType(new IntType())),
                new Composed(new New("v", new ValueExp(new IntValue(20))),
                        new Composed(new VariableDeclaration("a", new RefType(new RefType(new IntType()))),
                                new Composed(new New("a", new VarExp("v")),
                                        new Composed(new Print(new ReadHeapExp(new VarExp("v"))),
                                                new Composed(new Print(new ArithExp(new ReadHeapExp(new ReadHeapExp(new VarExp("a"))), new ValueExp(new IntValue(5)), '+')),
                                                        new Composed(new Print(new ReadHeapExp(new VarExp("v"))),
                                                                new Composed(new WriteHeap("v", new ValueExp(new IntValue(30))),
                                                                        new Print(new ReadHeapExp(new VarExp("v"))))))))))));
    }

    private void generateState6() {
        this.generatedStatements.add(new Composed(new VariableDeclaration("v", new RefType(new IntType())),
                new Composed(new New("v", new ValueExp(new IntValue(20))),
                        new Composed(new VariableDeclaration("a", new RefType(new RefType(new IntType()))),
                                new Composed(new New("a", new VarExp("v")),
                                        new Composed(new New("v", new ValueExp(new IntValue(30))),
                                                new Composed(new Print(new ReadHeapExp(new ReadHeapExp(new VarExp("a")))),
                                                        new New("v", new ValueExp(new IntValue(50))))))))));
    }

    private void generateState7() {
        this.generatedStatements.add(new Composed(new VariableDeclaration("v", new IntType()),
                new Composed(new Assign("v", new ValueExp(new IntValue(4))),
                        new Composed(new While(new RelationalExp(new VarExp("v"), new ValueExp(new IntValue(0)), GREATER),
                                new Assign("v", new ArithExp(new VarExp("v"), new ValueExp(new IntValue(1)), '-'))),
                                new Print(new VarExp("v"))))));
    }

    private void generateState8() {
        this.generatedStatements.add(new Composed(
          new VariableDeclaration("v", new IntType()), new Composed(
                  new VariableDeclaration("a", new RefType(new IntType())), new Composed(
                          new Assign("v", new ValueExp(new IntValue(10))), new Composed(
                                  new New("a", new ValueExp(new IntValue(22))), new Composed(
                                          new Fork(new Composed(
                                                  new WriteHeap("a", new ValueExp(new IntValue(30))), new Composed(
                                                          new Assign("v", new ValueExp(new IntValue(32))), new Composed(
                                                                  new Print(new VarExp("v")),
                                                                    new Print(new ReadHeapExp(new VarExp("a")))
                                          )
                                          )
                                          )
                                          ), new Composed(
                                                  new Print(new VarExp("v")), new Print(new ReadHeapExp(new VarExp("a")))
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
                  new New("v", new ValueExp(new IntValue(20))), new Composed(
                          new VariableDeclaration("a", new RefType(new RefType(new IntType()))), new Composed(
                                  new New("a", new VarExp("v")), new Composed(
                                      new Fork(
                                              new Composed(
                                                      new VariableDeclaration("b", new RefType(new RefType(new IntType()))), new Composed(
                                                        new New("v", new ValueExp(new IntValue(30))),
                                                        new New("b", new VarExp("v"))
                                              )
                                              )
                                      ), new Composed(
                                              new Print(new ReadHeapExp(new VarExp("v"))), new Print(new ReadHeapExp(new ReadHeapExp(new VarExp("a"))))
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
            initialStatement.typeCheck(new Dictionary<>());
        } catch (StmtException e) {
            throw new RepoException(e.getMessage());
        }
        this.programs.clear();
        this.programs.add(new ProgramState(new ExeStack<>(), new SymTable<>(), new Out<>(), initialStatement, new FileTable<>(), new Heap(), 0));
    }
}
