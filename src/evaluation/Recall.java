package evaluation;

import core.Instance;
import java.util.List;
import java.util.Objects;

public class Recall implements EvaluationMeasure<Double, Boolean> {
    public Recall() {
    }

    public double evaluate(List<Instance<Double, Boolean>> instances, List<Boolean> predictions) {
        int truePositives = 0;
        int falseNegative = 0;

        for(int i = 0; i < instances.size(); ++i) {
            if ((Boolean)predictions.get(i)) {
                if (Objects.equals(((Instance)instances.get(i)).getOutput(), predictions.get(i))) {
                    ++truePositives;
                }
            } else if ((Boolean)((Instance)instances.get(i)).getOutput()) {
                ++falseNegative;
            }
        }

        return (double)truePositives / (double)(truePositives + falseNegative);
    }
}
