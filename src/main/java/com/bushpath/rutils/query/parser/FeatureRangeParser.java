package com.bushpath.rutils.query.parser;

import com.bushpath.rutils.query.AndExpression;
import com.bushpath.rutils.query.Expression;
import com.bushpath.rutils.query.GreaterEqualExpression;
import com.bushpath.rutils.query.LessEqualExpression;
import com.bushpath.rutils.query.Query;

import java.util.Arrays;
import java.util.HashMap;

public class FeatureRangeParser extends Parser {
    public Query evaluate(String... arguments) throws Exception {
        // initialize expressions
        HashMap<String, Expression> expressions = new HashMap();

        // if arguments is null -> return empty query
        if (arguments == null) {
            return new Query(null, expressions);
        }

        // parse arguments
        for (String argument : arguments) {
            // parse feature
            String[] featureFields = argument.split(":");
            String feature = featureFields[0];

            // parse ranges
            String[] rangeFields = featureFields[1].split("\\.\\.");
            Expression<Float> expression = null;
            if (rangeFields.length > 0 && !rangeFields[0].isEmpty()) {
                expression = new GreaterEqualExpression(Float.parseFloat(rangeFields[0]));
            }

            if (rangeFields.length > 1 && !rangeFields[1].isEmpty()) {
                Expression e = 
                    new LessEqualExpression(Float.parseFloat(rangeFields[1]));

                if (expression == null) {
                    expression = e;
                } else {
                    expression = new AndExpression(expression, e);
                }
            }

            // check if expression == null
            if (expression == null) {
                throw new IllegalArgumentException("Feature '" + feature
                    + "' requires at least one range bound");
            }

            // add expression to map
            if (expressions.containsKey(feature)) {
                throw new IllegalArgumentException("Feature '" + feature
                    + "' cannot exist more than once per query");
            }

            expressions.put(feature, expression);
        }

        return new Query(null, expressions);
    }
}
