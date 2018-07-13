package com.bushpath.rutils.query;

import com.bushpath.rutils.quantize.Quantizer;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class Query implements Serializable {
    protected Map<String, Expression> expressions;

    public Query(Map<String, Expression> expressions) {
        this.expressions = expressions;
    }

    public boolean containsFeature(String feature) {
        return this.expressions.containsKey(feature);
    }

    public Collection<String> getFeatures() {
        return this.expressions.keySet();
    }

    public Expression getExpression(String feature) {
        return this.expressions.get(feature);
    }

    public int featureCount() {
        return this.expressions.size();
    }

    public Query bin(Map<String, float[]> binBoundaries) throws Exception {
        Map<String, Expression> expressions = new HashMap();
        for (Map.Entry<String, Expression> entry : this.expressions.entrySet()) {
            // bin expression
            Quantizer quantizer = new Quantizer(binBoundaries.get(entry.getKey()));
            Expression expression = entry.getValue().bin(quantizer);

            // add to new expressions
            expressions.put(entry.getKey(), expression);
        }

        return new Query(expressions);
    }
}
