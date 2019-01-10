package com.bushpath.rutils.query;

import com.bushpath.rutils.quantize.Quantizer;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class Query implements Serializable {
    protected String entity;
    protected HashMap<String, Expression> expressions;

    public Query(String entity,
            HashMap<String, Expression> expressions) {
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

	public static Query fromInputStream(InputStream in)
		    throws Exception {
        ObjectInputStream objectIn = new ObjectInputStream(in);
        Query query = (Query) objectIn.readObject();
        objectIn.close();

        return query;
	}

    public byte[] toByteArray() throws Exception {
		ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
		ObjectOutputStream objectOut = new ObjectOutputStream(byteOut);
		objectOut.writeObject(this);
		objectOut.close();
		byteOut.close();
	
		return byteOut.toByteArray();
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
