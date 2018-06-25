package com.bushpath.rutils.quantize;

public class Quantizer {
    protected float[] binBoundaries;

    public Quantizer(float[] binBoundaries) {
        this.binBoundaries = binBoundaries;
    }

    public double getRmse(float[] data, float[] midpoints) throws Exception {
        // compute mean squared error on quantized values
        double mean = 0.0;
        for (int i=0; i<data.length; i++) {
            int bin = this.evaluate(data[i]);
            double errorSquared = Math.pow(midpoints[bin] - data[i], 2);

            mean += (errorSquared - mean) / (i + 1);
        }

        // return square root of mean squared error
        return Math.sqrt(mean);
    }

    public double getNormalizedRmse(float[] data, float[] midpoints,
            float minimum, float maximum) throws Exception {

        // return normalized mean squared error
        return this.getRmse(data, midpoints) / (maximum - minimum);
    }

    public byte evaluate(float value) throws Exception {
        if (value < this.binBoundaries[0]
                || value > this.binBoundaries[this.binBoundaries.length-1]) {
            throw new IllegalArgumentException("Value '" + value
                + "' is not bounded by any bin");
        }

        // binary search
        byte first = 0, last = (byte) (binBoundaries.length-1), middle;
        while (last - first != 1) {
            middle = (byte) (first + ((last - first) / 2));
            if (value >= this.binBoundaries[middle]) {
                first = middle;
            } else {
                last = middle;
            }
        }

        return first;
    }
}
