package com.example.toylanguageinterpreter;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import model.exception.RepoException;
import model.statement.Statement;
import repository.Repository;
import repository.MemoryRepository;

import java.io.IOException;

public class MenuLogic {
    private Repository repository;

    @FXML
    private ListView<Statement> programs;

    @FXML
    public void initialize() {
        this.repository = new MemoryRepository("src/main/java/files/log.out");
        this.programs.setItems(FXCollections.observableArrayList(this.repository.getGeneratedStatements()));
    }

    @FXML
    protected void onRunButtonClick() {
        try {
            ObservableList<Integer> indices = this.programs.getSelectionModel().getSelectedIndices();

            if (!indices.isEmpty()) {
                this.repository.setState(indices.getFirst());

                this.createProgramWindow();
            }
        } catch (RepoException | IOException e) {
            System.out.println(e.getMessage());
            System.exit(1);
        }
    }

    private void createProgramWindow() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Interpreter.class.getResource("program.fxml"));
        ProgramLogic logic = new ProgramLogic(this.repository);
        fxmlLoader.setController(logic);

        Scene scene = new Scene(fxmlLoader.load(), 800, 600);

        Stage stage = new Stage();
        stage.setTitle("Program");
        stage.setScene(scene);

        stage.show();
    }
}