package com.bushpath.rutils.query;

import com.bushpath.rutils.quantize.Quantizer;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class Query implements Serializable {
    protected String entity;
    protected HashMap<String, Expression> expressions;

    public Query(String entity, HashMap<String, Expression> expressions) {
        this.entity = entity;
        this.expressions = expressions;
    }

    public void setEntity(String entity) {
        this.entity = entity;
    }

    public String getEntity() {
        return this.entity;
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

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder("query: " + this.entity + "\n");
        for (Map.Entry<String, Expression> entry : this.expressions.entrySet()) {
            stringBuilder.append(entry.getKey() + ":");
            stringBuilder.append(entry.getValue().toString(1));
        }

        return stringBuilder.toString();
    }
}
