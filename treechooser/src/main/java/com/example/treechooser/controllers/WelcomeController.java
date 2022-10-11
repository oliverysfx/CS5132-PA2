package com.example.treechooser.controllers;

import com.example.treechooser.Router;
import javafx.fxml.FXML;

public class WelcomeController {
    @FXML
    public void toFormPage() {
        Router.instance.push("fxml/form.fxml", null);
    }
}
