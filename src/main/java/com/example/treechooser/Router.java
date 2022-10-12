package com.example.treechooser;

import com.example.treechooser.controllers.Controller;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.util.ArrayList;

public class Router {
    public static Router instance = new Router();
    private Pane root;
    private final ArrayList<Parent> history = new ArrayList<>();
    public String modelFilePath; // quick fix

    private Router() {}

    public void setRoot(Pane newRoot) {
        root = newRoot;
    }

    public void push(String fxmlPath, Object data) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent page = loader.load();
            ((Controller) loader.getController()).loadData(data);
            root.getChildren().set(0, page);
            history.add(page);
        } catch (IOException exception) {
            System.out.println("Failed to load fxml: " + exception.getMessage());
        }
    }

    public void pop() {
        history.remove(history.size() - 1);
        Parent page = history.get(history.size() - 1);
        root.getChildren().set(0, page);
    }
}
