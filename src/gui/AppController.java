package gui;

import core.Instance;
import data.DataLoader;
import data.DataSplitter;
import evaluation.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import models.KNN;
import models.LogisticRegression;
import models.Perceptron;
import utils.IDistance;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class AppController {
    @FXML
    private Button buttonTest;

    @FXML
    private Button buttonTrain;

    @FXML
    private Button buttonChooseFile;

    @FXML
    private Label labelAccuracy;

    @FXML
    private Label labelF1;

    @FXML
    private Label labelFN;

    @FXML
    private Label labelFP;

    @FXML
    private Label labelPrecission;

    @FXML
    private Label labelRecall;

    @FXML
    private Label labelTN;

    @FXML
    private Label labelTP;

    @FXML
    private Label labelDataSource;

    @FXML
    private TabPane tabPaneModels;

    @FXML
    private Slider sliderTrainRatio;

    @FXML
    private Spinner<Integer> spinnerTrainRatio;



    private int openedTabIdx;
    private List<Instance<Double, Boolean>> trainData;
    private List<Instance<Double, Boolean>> testData;

    private KNN knn;
    private Perceptron perceptron;
    private LogisticRegression logisticRegression;

    private final Accuracy accuracy = new Accuracy();
    private final Precission precission = new Precission();
    private final Recall recall = new Recall();
    private final F1Score f1Score = new F1Score();
    private final ConfusionMatrix confusionMatrix = new ConfusionMatrix();

    private String filePath;
    private List<Instance<Double, Boolean>> instances;

    public void initialize(){
        //instances = DataLoader.loadCSV(filePath);

        loadTabContent(0, "KNNGUI.fxml");
        loadTabContent(1, "PerceptronGUI.fxml");
        loadTabContent(2, "LogisticRegressionGUI.fxml");

        tabPaneModels.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> {
            openedTabIdx = newValue.intValue();
        });

        SpinnerValueFactory<Integer> defaultRatio = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100, 70);
        spinnerTrainRatio.setValueFactory(defaultRatio);

        sliderTrainRatio.setValue(70);
        sliderTrainRatio.valueProperty().addListener((obs, oldVal, newVal) -> {
            spinnerTrainRatio.getValueFactory().setValue(newVal.intValue());
        });

        spinnerTrainRatio.valueProperty().addListener((obs, oldVal, newVal) -> {
            sliderTrainRatio.setValue(newVal);
        });


        buttonTrain.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                double trainRatio = getTrainRatio();
                double validateRatio = 0.0;

                DataSplitter<Instance<Double, Boolean>> splitter = new DataSplitter<>(trainRatio, validateRatio);
                splitter.splitData(instances);

                trainData = splitter.getTrainData();
                testData = splitter.getTestData();

                System.out.println("Train data size: " + trainData.size());
                System.out.println("Test data size: " + testData.size());

                // For perceptron and LR
                int nrOfParam = trainData.get(0).getInput().size();
                List<Double> weights = new ArrayList<>(nrOfParam);
                for (int i = 0; i < nrOfParam; i++) weights.add(0.0);

                System.out.println("Training data with " + trainRatio + " ratio");

                switch (openedTabIdx) {
                    case 0:
                        // Training a KNN
                        int neighbours = KNNController.getKValue();
                        IDistance<Double, Boolean> distance = KNNController.getDistanceMetric();

                        knn = new KNN(neighbours, distance);
                        knn.train(trainData);

                        System.out.println("KNN trained (" + neighbours + ") neighbors");
                        break;
                    case 1:
                        // Training a Perceptron
                        double learningRateP =PerceptronController.getLearningRate();
                        int cyclesP = PerceptronController.getCycles();

                        perceptron = new Perceptron(learningRateP, weights, cyclesP);
                        perceptron.train(trainData);

                        System.out.println("Perceptron trained (LR = " + learningRateP + "; C = " + cyclesP + ")");
                        break;
                    case 2:
                        // Training a Logistic Regression
                        double learningRateLR = LogisticRegressionController.getLearningRate();
                        int cyclesLR = LogisticRegressionController.getCycles();

                        logisticRegression = new LogisticRegression(learningRateLR, weights, cyclesLR);
                        logisticRegression.train(trainData);

                        System.out.println("Logistic regression trained (LR = " + learningRateLR + "; C = " + cyclesLR + ")");
                        break;
                    default:
                        break;
                }
            }
        });

        buttonTest.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                switch (openedTabIdx) {
                    case 0:
                        // Testing the KNN
                        knn.test(testData);
                        loadEvaluations(testData, knn.getPredictedLabels());

                        System.out.println("KNN tested");
                        break;
                    case 1:
                        // Testing a Perceptron
                        perceptron.test(testData);
                        loadEvaluations(testData, perceptron.getPredictedLabels());

                        System.out.println("Perceptron tested");
                        break;
                    case 2:
                        // Testing a logistic regression
                        logisticRegression.test(testData);
                        loadEvaluations(testData, logisticRegression.getPredictedLabels());

                        System.out.println("Logistic regression tested");
                        break;
                    default:
                        break;
                }
            }
        });

        buttonChooseFile.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Choose CSV File");

                fileChooser.setInitialDirectory(new File(System.getProperty("user.dir")));

                fileChooser.getExtensionFilters().addAll(
                        new FileChooser.ExtensionFilter("CSV Files", "*.csv")
                );

                Stage stage = (Stage) ((javafx.scene.Node) mouseEvent.getSource()).getScene().getWindow();
                File selectedFile = fileChooser.showOpenDialog(stage);

                if (selectedFile != null) {
                    System.out.println("Selected CSV file: " + selectedFile.getAbsolutePath());

                    // Calculate the relative path
                    Path currentPath = Paths.get(System.getProperty("user.dir"));
                    Path filePath = selectedFile.toPath();
                    Path relativePath = currentPath.relativize(filePath);

                    // Append the relative path to the label
                    String currentText = labelDataSource.getText();
                    labelDataSource.setText(currentText + " " + relativePath);

                    instances = DataLoader.loadCSV(selectedFile.getAbsolutePath());

                    if (!instances.isEmpty()) {
                        System.out.println("Data loaded successfully. Total instances: " + instances.size());
                        System.out.println("Data is ready for train-test split.");
                    } else {
                        System.out.println("Failed to load data from the selected file.");
                    }
                } else {
                    System.out.println("No file selected.");
                }
            }
        });
    }

    private void loadTabContent(int tabIndex, String fxmlFile) {
        try {
            Tab tab = tabPaneModels.getTabs().get(tabIndex);

            if (fxmlFile != null) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
                Node content = loader.load();
                tab.setContent(content);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error loading tab content: " + fxmlFile, e);
        }
    }

    private void loadEvaluations(List<Instance<Double, Boolean>> instances, List<Boolean> predictions){
        labelAccuracy.setText(String.format("%.5f",accuracy.evaluate(instances, predictions)));
        labelPrecission.setText(String.format("%.5f",precission.evaluate(instances, predictions)));
        labelRecall.setText(String.format("%.5f",recall.evaluate(instances, predictions)));
        labelF1.setText(String.format("%.5f",f1Score.evaluate(instances, predictions)));

        int[][] confMatrixValues = confusionMatrix.computeMatrix(instances, predictions);

        labelTP.setText(String.valueOf(confMatrixValues[0][0]));
        labelFP.setText(String.valueOf(confMatrixValues[0][1]));
        labelTN.setText(String.valueOf(confMatrixValues[1][0]));
        labelFN.setText(String.valueOf(confMatrixValues[1][1]));
    }

    private double getTrainRatio(){
        double ratio = 80;
        if (sliderTrainRatio.getValue() != 0) {
            ratio = (double) sliderTrainRatio.getValue() / 100;
        }
        return ratio;
    }
}