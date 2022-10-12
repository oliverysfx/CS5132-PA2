package com.example.treechooser;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        StackPane root = new StackPane();
        root.getStylesheets().add(getClass().getResource("styles/main.css").toExternalForm());

        Router.instance.setRoot(root);
        // hacky fix
        // for some reason this path cannot be loaded from the FormController class but it can be here
        Router.instance.modelFilePath = getClass().getResource("models/saved_tree.txt").getPath();

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("fxml/welcome.fxml"));
        root.getChildren().add(fxmlLoader.load());

        Scene scene = new Scene(root);
        stage.setTitle("Tree chooser");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}