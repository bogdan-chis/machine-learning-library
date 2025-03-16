package evaluation;

import core.Instance;
import java.util.List;

public interface EvaluationMeasure<F, L> {
    double evaluate(List<Instance<F, L>> var1, List<L> var2);
}
