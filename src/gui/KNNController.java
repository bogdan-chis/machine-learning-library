package gui;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import utils.EuclideanDistance;
import utils.IDistance;
import utils.ManhattanDistance;

public class KNNController {

    @FXML
    private RadioButton radioEuclidean;

    @FXML
    private RadioButton radioManhattan;

    @FXML
    private TextField textFieldNeighbors;

    @FXML
    private Button buttonDefault;

    private static int k;
    private static IDistance<Double, Boolean> distanceMetric;

    public void initialize() {
        System.out.println("KNN controller running");

        setDefaultHyperParams();
        if (!textFieldNeighbors.getText().isEmpty()) {
            try {
                k = Integer.parseInt(textFieldNeighbors.getText());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input for k. Using default value: " + k);
            }
        }

        textFieldNeighbors.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && !newValue.trim().isEmpty()) {
                try {
                    k = Integer.parseInt(newValue);
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input for k. Reverting to previous value: " + k);
                    textFieldNeighbors.setText(String.valueOf(k));
                }
            }
        });

        buttonDefault.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                setDefaultHyperParams();
                clearHyperParams();
            }
        });

    }

    public static int getKValue() {
        return k;
    }

    private void setDefaultHyperParams(){
        k = 27;
        distanceMetric = new EuclideanDistance();
    }

    public static IDistance<Double, Boolean> getDistanceMetric(){
        return distanceMetric;
    }

    public void onDistanceSelection() {
        if (radioEuclidean.isSelected()) {
            distanceMetric = new EuclideanDistance();
            System.out.println("Selected Euclidean Distance");
        } else if (radioManhattan.isSelected()) {
            distanceMetric = new ManhattanDistance();
            System.out.println("Selected Manhattan Distance");
        }
    }

    private void clearHyperParams(){
        textFieldNeighbors.clear();
        radioEuclidean.setSelected(true);
        radioManhattan.setSelected(false);
    }
}