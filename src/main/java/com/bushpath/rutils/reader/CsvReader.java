package com.bushpath.rutils.reader;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Collection;

public class CsvReader extends Reader {
    protected BufferedReader in;
    protected String[] features;
    protected int[] indices;

    public CsvReader(String file, Collection<String> features,
            String[] header) throws Exception {
        // initialize BufferedReader
        this.in = new BufferedReader(new FileReader(file));

        // parse header
        if (header == null || header.length == 0) {
            header = this.in.readLine().split(",");
        }

        // initialize indices and features
        if (features == null || features.isEmpty()) {
            this.indices = new int[header.length];
            for (int i=0; i<header.length; i++) {
                this.indices[i] = i;
            }

            this.features = header;
        } else {
            this.indices = new int[features.size()];

            int featureIndex = 0;
            for (String feature : features) {
                // find index of feature in header
                int index = -1;
                for (int j=0; j<header.length; j++) {
                    if (feature.equals(header[j])) {
                        index = j;
                        break;
                    }
                }

                if (index == -1) {
                    throw new Exception("Feature '" + feature
                        + "' not found in file '" + file + "'");
                }

                this.indices[featureIndex++] = index;
            }

            this.features = (String[]) features.toArray();
        }
    }

    @Override
    public double[] next() throws Exception {
        // read line
        String line = this.in.readLine();
        if (line == null) {
            return null;
        }

        // parse line
        String[] fields = line.split(",");
        double[] record = new double[this.indices.length];
        for (int i=0; i<indices.length; i++) {
            record[i] = Double.parseDouble(fields[this.indices[i]]);
        }

        return record;
    }

    public String[] getFeatures() {
        return this.features;
    }

    @Override
    public void close() throws Exception {
        if (this.in != null) {
            this.in.close();
        }
    }
}
