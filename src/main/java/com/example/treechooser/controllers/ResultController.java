package com.example.treechooser.controllers;

import javafx.fxml.FXML;
import javafx.scene.text.Text;

public class ResultController implements Controller {
    @FXML
    private Text speciesNameText;

    @Override
    public void loadData(Object data) {
        // data: String representing the species of tree
        speciesNameText.setText((String)data);
    }
}
