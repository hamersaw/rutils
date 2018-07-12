package com.bushpath.rutils.query;

import java.lang.Comparable;

public abstract class Expression<T extends Comparable<T>> {
    public abstract boolean evaluate(T value);
}
