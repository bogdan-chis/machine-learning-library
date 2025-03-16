package evaluation;

import core.Instance;
import java.util.List;
import java.util.Objects;

public class F1Score implements EvaluationMeasure<Double, Boolean> {
    public F1Score() {
    }

    public double evaluate(List<Instance<Double, Boolean>> instances, List<Boolean> predictions) {
        int truePositives = 0;
        int falsePositives = 0;
        int falseNegatives = 0;

        for(int i = 0; i < instances.size(); ++i) {
            if ((Boolean)predictions.get(i)) {
                if (Objects.equals(((Instance)instances.get(i)).getOutput(), predictions.get(i))) {
                    ++truePositives;
                } else {
                    ++falsePositives;
                }
            } else if ((Boolean)((Instance)instances.get(i)).getOutput()) {
                ++falseNegatives;
            }
        }

        double precision = (double)truePositives / (double)(truePositives + falsePositives);
        double recall = (double)truePositives / (double)(truePositives + falseNegatives);
        return 2.0 * precision * recall / (precision + recall);
    }
}
