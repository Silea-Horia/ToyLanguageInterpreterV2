package repository;

import model.exception.RepoException;
import model.state.ProgramState;
import model.statement.Statement;

import java.util.ArrayList;
import java.util.List;

public interface IRepository {
    void addPrgState(ProgramState state);
    void logPrgState(ProgramState state) throws RepoException;
    void setState(int option) throws RepoException;
    List<ProgramState> getPrgList();
    void setPrgList(List<ProgramState> newList);
    ArrayList<Statement> getGeneratedStatements();
}
