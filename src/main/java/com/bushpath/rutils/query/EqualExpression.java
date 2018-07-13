package com.bushpath.rutils.query;

import com.bushpath.rutils.quantize.Quantizer;

import java.io.Serializable;
import java.lang.Comparable;

public class EqualExpression<T extends Comparable<T>> extends Expression<T>
        implements Serializable {
    private T value;

    public EqualExpression(T value) {
        super();
        this.value = value;
    }

    public boolean evaluate(T value) {
        return value.compareTo(this.value) == 0;
    }

    @Override
    public Expression bin(Quantizer quantizer) throws Exception {
        byte value = quantizer.evaluate((Float) this.value);
        return new EqualExpression<Byte>(value);
    }
}
