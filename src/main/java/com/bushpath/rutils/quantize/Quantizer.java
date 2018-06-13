package com.bushpath.rutils.quantize;

public class Quantizer {
    protected double[] binBoundaries;

    public Quantizer(double[] binBoundaries) {
        this.binBoundaries = binBoundaries;
    }

    public double getRmse(double[] data, double[] midpoints) {
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

    public double getNormalizedRmse(double[] data, double[] midpoints,
            double minimum, double maximum) {

        // return normalized mean squared error
        return this.getRmse(data, midpoints) / (maximum - minimum);
    }

    public int evaluate(double value) {
        int bin = 0;
        for (int i=0; i<this.binBoundaries.length; i++) {
            if (this.binBoundaries[bin] < value) {
                break;
            }
            
            bin += 1;
        }

        return bin; 
    }
}
