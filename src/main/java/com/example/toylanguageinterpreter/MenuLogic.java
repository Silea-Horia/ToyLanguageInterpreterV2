package com.example.toylanguageinterpreter;

import controller.Controller;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import model.exception.ControllerException;
import model.statement.Statement;
import repository.IRepository;
import repository.Repository;

import java.io.IOException;

public class MenuLogic {
    private IRepository repository;
    private Controller controller;

    @FXML
    private ListView<Statement> programs;

    @FXML
    public void initialize() {
        this.repository = new Repository("src/main/java/files/log.out");
        this.controller = new Controller(this.repository);
        this.programs.setItems(FXCollections.observableArrayList(this.repository.getGeneratedStatements()));
    }

    @FXML
    protected void onRunButtonClick() {
        try {
            ObservableList<Integer> indices = this.programs.getSelectionModel().getSelectedIndices();

            this.controller.generateInitialState(indices.getFirst()); //TODO HANDLE NO INDEX

            this.createProgramWindow();
        } catch (ControllerException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void createProgramWindow() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Interpreter.class.getResource("program.fxml"));
        fxmlLoader.setController(this.controller);

        Scene scene = new Scene(fxmlLoader.load(), 800, 600);

        Stage stage = new Stage();
        stage.setTitle("Program");
        stage.setScene(scene);

        stage.show();
    }
}