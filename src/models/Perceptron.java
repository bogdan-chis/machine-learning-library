package models;

import core.Instance;
import core.Model;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Perceptron implements Model<Double, Boolean> {
    private final double learningRate;
    private final List<Double> weights;
    private final List<Boolean> predictedLabels;
    private double bias;
    private final int cycles;

    public Perceptron(double learningRate, List<Double> weights, double bias) {
        this.learningRate = learningRate;
        this.weights = new ArrayList(weights);
        this.bias = bias;
        this.predictedLabels = new ArrayList();
        this.cycles = 3;
    }

    public Perceptron(double learningRate, List<Double> weights, double bias, int cycles) {
        this.learningRate = learningRate;
        this.weights = new ArrayList(weights);
        this.bias = bias;
        this.predictedLabels = new ArrayList();
        this.cycles = cycles;
    }

    public Perceptron(double learningRate, List<Double> weights, int cycles) {
        this.learningRate = learningRate;
        this.weights = new ArrayList(weights);
        this.bias = 0.0;
        this.predictedLabels = new ArrayList();
        this.cycles = cycles;
    }

    public void train(List<Instance<Double, Boolean>> instances) {
        boolean convergence = false;
        int iteration = 0;

        label46:
        while(!convergence && iteration < this.cycles) {
            convergence = true;
            ++iteration;
            Iterator var4 = instances.iterator();

            while(true) {
                Instance instance;
                int error;
                do {
                    if (!var4.hasNext()) {
                        continue label46;
                    }

                    instance = (Instance)var4.next();
                    double z = this.weightedSum(instance);
                    int predictionLabel = this.stepFunction(z) ? 1 : 0;
                    int actualLabel = (Boolean)instance.getOutput() ? 1 : 0;
                    error = actualLabel - predictionLabel;
                } while(error == 0);

                convergence = false;
                List<Double> input = instance.getInput();

                for(int i = 0; i < this.weights.size(); ++i) {
                    this.weights.set(i, (Double)this.weights.get(i) + this.learningRate * (double)error * (Double)input.get(i));
                }

                this.bias += this.learningRate * (double)error;
            }
        }

    }

    public void test(List<Instance<Double, Boolean>> instances) {
        Iterator var2 = instances.iterator();

        while(var2.hasNext()) {
            Instance<Double, Boolean> instance = (Instance)var2.next();
            double z = this.weightedSum(instance);
            Boolean predictedLabel = this.stepFunction(z);
            this.predictedLabels.add(predictedLabel);
        }

    }

    private double weightedSum(Instance<Double, Boolean> instance) {
        double sum = 0.0;
        List<Double> input = instance.getInput();

        for(int i = 0; i < this.weights.size(); ++i) {
            sum += (Double)this.weights.get(i) * (Double)input.get(i);
        }

        return sum + this.bias;
    }

    private boolean stepFunction(double z) {
        return z >= 0.0;
    }

    public List<Boolean> getPredictedLabels() {
        return this.predictedLabels;
    }
}
