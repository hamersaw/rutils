package com.bushpath.rutils.query;

import com.bushpath.rutils.quantize.Quantizer;

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
    public Expression bin(Quantizer quantizer) throws Exception {
        OrExpression orExpression = new OrExpression();
        for (Expression expression : this.expressions) {
            orExpression.addExpression(expression.bin(quantizer));
        }

        return orExpression;
    }
}
