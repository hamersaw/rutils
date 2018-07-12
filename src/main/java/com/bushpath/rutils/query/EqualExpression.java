package com.bushpath.rutils.query;

import java.lang.Comparable;

public class EqualExpression<T extends Comparable<T>> extends Expression<T> {
    private T value;

    public EqualExpression(T value) {
        super();
        this.value = value;
    }

    public boolean evaluate(T value) {
        return value.compareTo(this.value) == 0;
    }
}
