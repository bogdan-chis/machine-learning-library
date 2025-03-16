//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package main;

import core.Instance;
import data.DataLoader;
import data.DataSplitter;
import evaluation.Accuracy;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import models.KNN;
import models.Perceptron;

public class Main {
    public Main() {
    }

    public static void main(String[] args) {
        String filePath = "src/data/diabetes.csv";
        List<Instance<Double, Boolean>> instances = DataLoader.loadCSV(filePath);
        double trainRatio = 0.7;
        double validateRatio = 0.0;
        DataSplitter<Instance<Double, Boolean>> splitter = new DataSplitter(trainRatio, validateRatio);
        splitter.splitData(instances);
        List<Instance<Double, Boolean>> trainData = splitter.getTrainData();
        List<Instance<Double, Boolean>> testData = splitter.getTestData();
        KNN knn = new KNN(3);
        knn.train(trainData);
        knn.test(testData);
        int nrOfParam = ((Instance)trainData.get(0)).getInput().size();
        List<Double> weights = new ArrayList(nrOfParam);

        for(int i = 0; i < nrOfParam; ++i) {
            weights.add(0.0);
        }

        double learningRate = 0.001;
        double bias = 0.0;
        Perceptron perceptron = new Perceptron(learningRate, weights, bias);
        perceptron.train(trainData);
        perceptron.test(testData);
        Accuracy accuracy = new Accuracy();
        PrintStream var10000 = System.out;
        double var10001 = accuracy.evaluate(testData, knn.getPredictedLabels());
        var10000.println("KNN Accuracy: " + var10001);
        var10000 = System.out;
        var10001 = accuracy.evaluate(testData, perceptron.getPredictedLabels());
        var10000.println("Perceptron Accuracy: " + var10001);
    }
}
