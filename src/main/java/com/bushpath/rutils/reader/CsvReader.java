package com.bushpath.rutils.reader;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Collection;

public class CsvReader extends Reader {
    protected BufferedReader in;
    protected int[] indexes;

    public CsvReader(String file, Collection<String> features, String[] header) throws Exception {
        // initialize BufferedReader
        this.in = new BufferedReader(new FileReader(file));

        // initialize indexes
        this.indexes = new int[features.size()];

        // parse header
        if (header == null || header.length == 0) {
            header = this.in.readLine().split(",");
        }

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

            this.indexes[featureIndex++] = index;
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
        double[] record = new double[this.indexes.length];
        for (int i=0; i<indexes.length; i++) {
            record[i] = Double.parseDouble(fields[this.indexes[i]]);
        }

        return record;
    }

    @Override
    public void close() throws Exception {
        if (this.in != null) {
            this.in.close();
        }
    }
}
