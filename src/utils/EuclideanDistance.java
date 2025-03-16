package utils;

import core.Instance;
import java.util.List;

public class EuclideanDistance implements IDistance<Double, Boolean> {
    public EuclideanDistance() {
    }

    public double computeDistance(Instance<Double, Boolean> instance1, Instance<Double, Boolean> instance2) {
        List<Double> input1 = instance1.getInput();
        List<Double> input2 = instance2.getInput();
        double sum = 0.0;

        for(int i = 0; i < input1.size(); ++i) {
            double diff = (Double)input1.get(i) - (Double)input2.get(i);
            sum += diff * diff;
        }

        return Math.sqrt(sum);
    }
}
