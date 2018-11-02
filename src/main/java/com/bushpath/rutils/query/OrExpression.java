package com.bushpath.rutils.query;

import java.io.Serializable;
import java.lang.Comparable;
import java.util.HashSet;
import java.util.Set;

public class OrExpression<T extends Comparable<T>> extends Expression<T>
        implements Serializable {
    protected Set<Expression<T>> expressions;

    public OrExpression(Expression... expressions) {
        super();

        // initialize expressions
        this.expressions = new HashSet();
        for (Expression expression : expressions) {
            this.expressions.add(expression);
        }
    }

    public void addExpression(Expression expression) {
        this.expressions.add(expression);
    }

    @Override
    public boolean evaluate(T value) {
        for (Expression expression : this.expressions) {
            if (expression.evaluate(value)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean evaluateBin(T lowerBound, T upperBound) {
        for (Expression expression : this.expressions) {
            if (expression.evaluateBin(lowerBound, upperBound)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public String toString(int depth) {
        StringBuilder stringBuilder = new StringBuilder("\n");
        for (int i=0; i<depth; i++) {
            stringBuilder.append("\t");
        }

        stringBuilder.append("OrExpression");
        for (Expression expression : this.expressions) {
            stringBuilder.append(expression.toString(depth + 1));
        }

        return stringBuilder.toString();
    }
}
