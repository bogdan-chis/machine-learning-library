package core;

import java.util.List;

public interface Model<F, L> {
    void train(List<Instance<F, L>> var1);

    void test(List<Instance<F, L>> var1);
}
