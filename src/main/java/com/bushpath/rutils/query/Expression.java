package com.bushpath.rutils.query;

import com.bushpath.rutils.quantize.Quantizer;

import java.io.Serializable;
import java.lang.Comparable;

public abstract class Expression<T extends Comparable<T>> implements Serializable  {
    public abstract boolean evaluate(T value);
    public abstract Expression bin(Quantizer quantizer) throws Exception;
}
