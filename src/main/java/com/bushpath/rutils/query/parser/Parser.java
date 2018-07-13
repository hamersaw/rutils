package com.bushpath.rutils.query.parser;

import com.bushpath.rutils.query.Query;

public abstract class Parser {
    public abstract Query evaluate(String... arguments) throws Exception;
}
