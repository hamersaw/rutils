package com.bushpath.rutils.query;

import java.lang.Comparable;

public class LessEqualExpression<T extends Comparable<T>> extends Expression<T> {
    private T value;

    public LessEqualExpression(T value) {
        super();
        this.value = value;
    }

    public boolean evaluate(T value) {
        return value.compareTo(this.value) <= 0;
    }
}
