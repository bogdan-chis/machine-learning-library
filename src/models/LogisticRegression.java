package models;

import core.Instance;
import core.Model;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class LogisticRegression implements Model<Double, Boolean> {
    private final double learningRate;
    private final List<Double> weights;
    private double bias;
    private final int cycles;
    private final List<Boolean> predictedLabels;

    public LogisticRegression(double learningRate, List<Double> weights, int cycles) {
        this.learningRate = learningRate;
        this.weights = weights;
        this.cycles = cycles;
        this.bias = 0.0;
        this.predictedLabels = new ArrayList();
    }

    public void train(List<Instance<Double, Boolean>> instances) {
        int iteration = 0;
        int size = this.weights.size();
        double[] derivWeights = new double[size];
        double derivBias = 0.0;

        while(iteration < this.cycles) {
            ++iteration;

            for(Iterator var7 = instances.iterator(); var7.hasNext(); this.bias -= this.learningRate * (derivBias / (double)size)) {
                Instance<Double, Boolean> instance = (Instance)var7.next();
                double f = this.weightedSum(instance);
                double sigmoidResult = this.sigmoid(f);
                int output = (Boolean)instance.getOutput() ? 1 : 0;
                double error = sigmoidResult - (double)output;

                int i;
                for(i = 0; i < size; ++i) {
                    derivWeights[i] += error * (Double)instance.getInput().get(i);
                }

                derivBias += error;

                for(i = 0; i < size; ++i) {
                    this.weights.set(i, (Double)this.weights.get(i) - this.learningRate * (derivWeights[i] / (double)size));
                }
            }
        }

    }

    public void test(List<Instance<Double, Boolean>> instances) {
        Iterator var2 = instances.iterator();

        while(var2.hasNext()) {
            Instance<Double, Boolean> instance = (Instance)var2.next();
            double f = this.weightedSum(instance);
            double sigmoidResult = this.sigmoid(f);
            Boolean predictedLabel = sigmoidResult >= 0.5;
            this.predictedLabels.add(predictedLabel);
        }

    }

    public List<Boolean> getPredictedLabels() {
        return this.predictedLabels;
    }

    private double weightedSum(Instance<Double, Boolean> instance) {
        double sum = 0.0;
        List<Double> input = instance.getInput();

        for(int i = 0; i < this.weights.size(); ++i) {
            sum += (Double)this.weights.get(i) * (Double)input.get(i);
        }

        return sum + this.bias;
    }

    private double sigmoid(double f) {
        return 1.0 / (1.0 + Math.exp(-f));
    }
}
