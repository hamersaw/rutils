package com.bushpath.rutils.reader;

public abstract class Reader {
    public abstract double[] next() throws Exception;
    public abstract void close() throws Exception;
}
