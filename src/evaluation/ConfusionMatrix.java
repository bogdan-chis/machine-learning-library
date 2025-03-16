package evaluation;

import core.Instance;
import java.util.List;
import java.util.Objects;

public class ConfusionMatrix {
    public ConfusionMatrix() {
    }

    public int[][] computeMatrix(List<Instance<Double, Boolean>> instances, List<Boolean> predictions) {
        int truePositives = 0;
        int trueNegatives = 0;
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
            } else {
                ++trueNegatives;
            }
        }

        return new int[][]{{truePositives, falsePositives}, {trueNegatives, falseNegatives}};
    }
}
