package com.example.toylanguageinterpreter;

import controller.Controller;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import model.exception.ControllerException;
import model.statement.Statement;
import repository.IRepository;
import repository.Repository;

public class HelloController {
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
    protected void onHelloButtonClick() {
        try {
            this.controller.generateInitialState(8);
            System.out.println(this.repository.getPrgList().getFirst());
            this.controller.allStep();
        } catch (ControllerException e) {
            throw new RuntimeException(e);
        }
    }
}