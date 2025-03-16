package evaluation;

import core.Instance;
import java.util.List;
import java.util.Objects;

public class Precission implements EvaluationMeasure<Double, Boolean> {
    public Precission() {
    }

    public double evaluate(List<Instance<Double, Boolean>> instances, List<Boolean> predictions) {
        int truePositives = 0;
        int falsePositives = 0;

        for(int i = 0; i < instances.size(); ++i) {
            if ((Boolean)predictions.get(i)) {
                if (Objects.equals(((Instance)instances.get(i)).getOutput(), predictions.get(i))) {
                    ++truePositives;
                } else {
                    ++falsePositives;
                }
            }
        }

        return (double)truePositives / (double)(truePositives + falsePositives);
    }
}
