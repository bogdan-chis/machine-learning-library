package evaluation;

import core.Instance;
import java.util.List;

public class Accuracy implements EvaluationMeasure<Double, Boolean> {
    public Accuracy() {
    }

    public double evaluate(List<Instance<Double, Boolean>> instances, List<Boolean> predictions) {
        int correctPrecisions = 0;

        for(int i = 0; i < instances.size(); ++i) {
            if (((Boolean)((Instance)instances.get(i)).getOutput()).equals(predictions.get(i))) {
                ++correctPrecisions;
            }
        }

        return (double)correctPrecisions / (double)instances.size();
    }
}
