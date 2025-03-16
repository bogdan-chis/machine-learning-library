package gui;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

public class LogisticRegressionController {

    @FXML
    private Button buttonDefault;

    @FXML
    private TextField textFieldCycles;

    @FXML
    private TextField textFieldLearningRate;

    private static double learningRate;
    private static int cycles;

    public void initialize() {
        System.out.println("Logistic regression controller running");

        setDefaultHyperParams();

        textFieldLearningRate.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && !newValue.trim().isEmpty()) {
                try {
                    learningRate = Double.parseDouble(newValue);
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input for LR. Reverting to previous value: " + learningRate);
                    textFieldLearningRate.setText(String.valueOf(learningRate));
                }
            }
        });

        textFieldCycles.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && !newValue.trim().isEmpty()) {
                try {
                    cycles = Integer.parseInt(newValue);
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input for k. Reverting to previous value: " + cycles);
                    textFieldCycles.setText(String.valueOf(cycles));
                }
            }
        });

        buttonDefault.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                setDefaultHyperParams();
                clearTextField();
            }
        });


    }

    private void setDefaultHyperParams(){
        learningRate = 0.001;
        cycles = 5;
    }

    private void clearTextField(){
        textFieldCycles.clear();
        textFieldLearningRate.clear();
    }

    public static double getLearningRate() {
        return learningRate;
    }

    public static int getCycles(){
        return cycles;
    }
}