package com.example.toylanguageinterpreter;

import controller.Controller;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import repository.Repository;

public class ProgramLogic {
    private Repository repository;

    @FXML
    private TextField noPrograms;

    @FXML
    private Button oneStep;

    public ProgramLogic(Repository repository) {
        this.repository = repository;
    }

    @FXML
    public void initialize() {
        this.updateNoPrograms();
    }

    @FXML
    protected void runOneStep() {

    }

    private void updateNoPrograms() {
        this.noPrograms.setText(Integer.toString(this.repository.getNoPrograms()));
    }
}
