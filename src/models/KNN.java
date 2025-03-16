package models;

import core.Instance;
import core.Model;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import utils.EuclideanDistance;
import utils.IDistance;

public class KNN implements Model<Double, Boolean> {
    private final int k;
    private List<Instance<Double, Boolean>> trainingData;
    private List<Boolean> predictedLabels = new ArrayList();
    private IDistance<Double, Boolean> distance = new EuclideanDistance();

    public KNN(int k) {
        this.k = k;
        this.trainingData = new ArrayList();
        this.predictedLabels = new ArrayList();
    }

    public KNN(int k, IDistance<Double, Boolean> distance) {
        this.k = k;
        this.distance = distance;
    }

    public void train(List<Instance<Double, Boolean>> instances) {
        this.trainingData = new ArrayList(instances);
    }

    public void test(List<Instance<Double, Boolean>> instances) {
        Iterator var2 = instances.iterator();

        while(var2.hasNext()) {
            Instance<Double, Boolean> testInstance = (Instance)var2.next();
            Boolean predictedLabel = this.predict(testInstance);
            this.predictedLabels.add(predictedLabel);
        }

    }

    private Boolean predict(Instance<Double, Boolean> testInstance) {
        List<Instance<Double, Boolean>> neighbors = this.findKNearestNeighbors(testInstance);
        return this.majorityVote(neighbors);
    }

    private List<Instance<Double, Boolean>> findKNearestNeighbors(Instance<Double, Boolean> testInstance) {
        return this.trainingData.stream().sorted(Comparator.comparingDouble((instance) -> {
            return this.distance.computeDistance(instance, testInstance);
        })).limit((long)this.k).toList();
    }

    private boolean majorityVote(List<Instance<Double, Boolean>> instances) {
        Map<Boolean, Integer> labelCounts = new HashMap();
        Iterator var3 = instances.iterator();

        while(var3.hasNext()) {
            Instance<Double, Boolean> instance = (Instance)var3.next();
            boolean label = (Boolean)instance.getOutput();
            labelCounts.put(label, (Integer)labelCounts.getOrDefault(label, 0) + 1);
        }

        int trueCount = (Integer)labelCounts.getOrDefault(true, 0);
        int falseCount = (Integer)labelCounts.getOrDefault(false, 0);
        return trueCount >= falseCount;
    }

    private double EuclideanDistance(Instance<Double, Boolean> instance1, Instance<Double, Boolean> instance2) {
        List<Double> input1 = instance1.getInput();
        List<Double> input2 = instance2.getInput();
        double sum = 0.0;

        for(int i = 0; i < input1.size(); ++i) {
            double diff = (Double)input1.get(i) - (Double)input2.get(i);
            sum += diff * diff;
        }

        return Math.sqrt(sum);
    }

    public List<Boolean> getPredictedLabels() {
        return this.predictedLabels;
    }
}
