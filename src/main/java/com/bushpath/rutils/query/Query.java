package com.bushpath.rutils.query;

import java.lang.Comparable;
import java.util.HashSet;
import java.util.Set;

public class Query<T extends Comparable<T>> {
    protected Set<Expression<T>> expressions;

    public Query() {
        this.expressions = new HashSet();
    }

    public void addExpression(Expression<T> expression) {
        this.expressions.add(expression);
    }

    public boolean evaluate(T value) {
        for (Expression expression : this.expressions) {
            if (!expression.evaluate(value)) {
                return false;
            }
        }

        return true;
    }
}
