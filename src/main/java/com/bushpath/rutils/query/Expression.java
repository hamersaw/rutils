package com.bushpath.rutils.query;

import java.io.Serializable;
import java.lang.Comparable;

public abstract class Expression<T extends Comparable<T>> implements Serializable  {
    public abstract boolean evaluate(T value);
    public abstract boolean evaluateBin(T lowerBound, T upperBound);

    @Override
    public String toString() {
        return this.toString(0);
    }

    public abstract String toString(int depth);
}
