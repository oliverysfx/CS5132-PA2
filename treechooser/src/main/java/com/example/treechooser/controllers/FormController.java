package com.example.treechooser.controllers;

import com.example.treechooser.Router;
import com.example.treechooser.decisiontree.DecisionTree;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

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

    private final String[] SPECIES = {
            "European Silver Fir (Abies Alba)", "Silver Birch (Betula Pendula)", "European Beech (Fagus Sylvatica)",
            "Norway Spruce (Picea Abies)", "Swiss Pine (Pinus Cembra)", "Aleppo Pine (Pinus Halenpensis)",
            "Austrian Pine (Pinus Nigra)", "Black Poplar (Populus Nigra)", "Maritime Pine (Pinus Pinaster)",
            "Scots Pine (Pinus Sylvetris)", "Sessile Oak (Quercus Petraea)", "English Yew (Taxus Baccata)"
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
            String chosenSpecies = SPECIES[(int)Math.round(result)];
            Router.instance.push("fxml/result.fxml", chosenSpecies);
        } catch (NumberFormatException e) {
            new Alert(Alert.AlertType.ERROR, "Invalid value(s) detected. You can only enter numbers for the fields").show();
        }
    }
}
