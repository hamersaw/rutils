package com.bushpath.rutils.query;

import java.io.Serializable;
import java.lang.Comparable;

public class EqualExpression<T extends Comparable<T>> extends Expression<T>
        implements Serializable {
    private T value;

    public EqualExpression(T value) {
        super();
        this.value = value;
    }

    @Override
    public boolean evaluate(T value) {
        return value.compareTo(this.value) == 0;
    }

    @Override
    public boolean evaluateBin(T lowerBound, T upperBound) {
        return lowerBound.compareTo(this.value) <= 0 &&
            upperBound.compareTo(this.value) > 0;
    }

    @Override
    public String toString(int depth) {
        StringBuilder stringBuilder = new StringBuilder("\n");
        for (int i=0; i<depth; i++) {
            stringBuilder.append("\t");
        }

        stringBuilder.append("EqualExpression (" + this.value + ")");
        return stringBuilder.toString();
    }
}
