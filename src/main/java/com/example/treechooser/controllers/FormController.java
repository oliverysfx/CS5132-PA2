package com.example.treechooser.controllers;

import com.example.treechooser.Router;
import com.example.treechooser.decisiontree.DecisionTree;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Window;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Arrays;

public class FormController implements Controller {
    @FXML
    private TextField soilDepthField;

    @FXML
    private Slider rockCoverSlider;

    @FXML
    private Text rockCoverValueText;

    @FXML
    private TextField altitudeField;

    @FXML
    private TextField availableWaterField;

    @FXML
    private TextField humidityField;

    @FXML
    private TextField meanTempField;

    @FXML
    private TextField minTempField;

    @FXML
    private TextField maxTempField;

    @FXML
    private TextField numHotDaysField;

    @FXML
    private TextField numColdDaysField;

    @FXML
    private TextField availableSolarRadiationField;

    @Override
    public void loadData(Object data) {
        rockCoverSlider.valueProperty().addListener((obs, old, val) -> {
            StackPane sliderTrack = (StackPane) rockCoverSlider.lookup(".track");
            long percentage = Math.round((val.intValue()-1) * 16.67);
            String style = String.format(
                "-fx-background-color: linear-gradient(to right, #3B7427 %d%%, #2a2a2a %d%%);",
                percentage, percentage
            );
            sliderTrack.setStyle(style);

            final long roundedValue = Math.round(val.doubleValue());
            rockCoverSlider.valueProperty().set(roundedValue);
            rockCoverValueText.setText(roundedValue + "");
        });
    }

    public void showInputExplanations() {
        Dialog dialog = new Dialog<>();

        // javafx doesnt work
        Window window = dialog.getDialogPane().getScene().getWindow();
        window.setOnCloseRequest(event -> window.hide());

        dialog.setTitle("Input explanations");
        dialog.setHeaderText("Brief explanations some of the more confusing fields");
        Label label = new Label("rock cover (1: none, 2: little (5-20%), 3: low (20-40%), 4: medium (40-60%), 5: high (60-80%), 6: very high(80-95%), 7: full cover (>95%)),\n\ngrowing degree days = (number of days) * (degrees above 0°C). If there are 2 days at 20°C each, growing degree days = 40gdd. 1 day at -5°C = 0gdd\n\nSimilarly, number of frost days = (number of days) * (degrees below 0°)");
        label.setPrefWidth(400);
        label.setPrefHeight(200);
        label.setWrapText(true);
        dialog.getDialogPane().setContent(label);
        dialog.getDialogPane().getButtonTypes().addAll(new ButtonType("Understood", ButtonBar.ButtonData.OK_DONE));
        dialog.show();
    }

    private final String[] SPECIES = {
        "European Silver Fir", "Silver Birch", "European Beech",
        "Norway Spruce", "Swiss Pine", "Aleppo Pine",
        "Austrian Pine", "Black Poplar", "Maritime Pine",
        "Scots Pine", "Sessile Oak", "English Yew"
    };

    @FXML
    public void suggestTree() {
        try {
            double soilDepth = Double.parseDouble(soilDepthField.getText());
            double rockCover = Math.round(rockCoverSlider.getValue());
            double altitude = Double.parseDouble(altitudeField.getText());
            double availableWater = Double.parseDouble(availableWaterField.getText());
            double meanTemp = Double.parseDouble(meanTempField.getText());
            double maxTemp = Double.parseDouble(maxTempField.getText());
            double minTemp = Double.parseDouble(minTempField.getText());
            double numHotDays = Double.parseDouble(numHotDaysField.getText());
            double numColdDays = Double.parseDouble(numColdDaysField.getText());
            double humidity = Double.parseDouble(humidityField.getText());
            double availableSolarRadiation = Double.parseDouble(availableSolarRadiationField.getText());

            DecisionTree tree = DecisionTree.read(Router.instance.modelFilePath);
            double result = tree.predict(new double[] {
                soilDepth,
                rockCover,
                altitude,
                availableWater,
                meanTemp,
                maxTemp,
                minTemp,
                numHotDays,
                numColdDays,
                humidity,
                availableSolarRadiation,
            });
            String chosenSpecies = SPECIES[(int)result];
            Router.instance.push("fxml/result.fxml", chosenSpecies);
        } catch (NumberFormatException e) {
            new Alert(Alert.AlertType.ERROR, "Invalid value(s) detected. You can only enter numbers for the fields").show();
        }
    }
}
