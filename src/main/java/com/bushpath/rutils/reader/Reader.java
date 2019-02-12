package com.bushpath.rutils.reader;

public abstract class Reader<T> {
    public abstract String[] getHeader();
    public abstract T next() throws Exception;
    public abstract void close() throws Exception;
}
