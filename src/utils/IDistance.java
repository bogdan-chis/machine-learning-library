package utils;

import core.Instance;

public interface IDistance<T, E> {
    double computeDistance(Instance<T, E> var1, Instance<T, E> var2);
}
