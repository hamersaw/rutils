package com.bushpath.rutils.statistics;

import java.io.Serializable;

public class OnlineStatistics implements Serializable {
    protected long count;
    protected int dimensionality;
    protected float[] minimums;
    protected float[] maximums;
    protected float[] means;
    protected float[] m2;

    public OnlineStatistics() {
        this.count = 0;
    }

    public void update(float[] data) throws Exception {
        if (this.count == 0) {
            // initialize
            this.dimensionality = data.length;
            this.minimums = new float[this.dimensionality];
            this.maximums = new float[this.dimensionality];
            this.means = new float[this.dimensionality];
            this.m2 = new float[this.dimensionality];

            for (int i=0; i<this.dimensionality; i++) {
                this.minimums[i] = Float.MAX_VALUE;
                this.maximums[i] = Float.MIN_VALUE;
                this.means[i] = 0.0f;
                this.m2[i] = 0.0f;
            }
        }

        // validate data dimensionality
        if (data.length != this.dimensionality) {
            throw new IllegalArgumentException("Input data (" + data.length
                + ") doesn't have the same dimensionality as this structure ("
                + this.dimensionality + ")");
        }

        // update this structure
        this.count += 1;
        for (int i=0; i<this.dimensionality; i++) {
            // update minimum and maximum
            if (data[i] < this.minimums[i]) {
                this.minimums[i] = data[i];
            }

            if (data[i] > this.maximums[i]) {
                this.maximums[i] = data[i];
            }

            // update mean
            float delta = data[i] - this.means[i];
            this.means[i] += delta / this.count;
            float deltaTwo = data[i] - this.means[i];
            this.m2[i] += delta * deltaTwo;
        }
    }

    public void merge(OnlineStatistics statistics) throws Exception {
        if (this.count == 0) {
            this.count = statistics.count;;
            this.dimensionality = statistics.dimensionality;
            this.minimums = statistics.minimums.clone();
            this.maximums = statistics.maximums.clone();
            this.means = statistics.means.clone();
            this.m2 = statistics.m2.clone();

            return;
        }

        if (statistics.count == 0) {
            return;
        }

        // validate data dimensionality
        if (this.dimensionality != statistics.dimensionality) {
            throw new IllegalArgumentException("This structure (" + this.dimensionality
                + ") doesn't have the same dimensionality the merge structure ("
                + statistics.getDimensionality() + ")");
        }

        // update this structure
        for (int i=0; i<this.dimensionality; i++) {
            // update minimum and maximum
            if (this.minimums[i] > statistics.minimums[i]) {
                this.minimums[i] = statistics.minimums[i];
            }

            if (this.maximums[i] < statistics.maximums[i]) {
                this.maximums[i] = statistics.maximums[i];
            }

            // update mean
            float delta = this.means[i] - statistics.means[i];
            this.means[i] = ((this.means[i] * this.count) + (statistics.means[i] * statistics.count)) / (this.count + statistics.count);

            // update m2
            this.m2[i] += statistics.m2[i] + delta * delta * this.count + statistics.count / (this.count + statistics.count);
        } 

        // update count
        this.count += statistics.count;
    }

    public long getCount() {
        return this.count;
    }

    public int getDimensionality() {
        return this.dimensionality;
    }

    public float[] getMinimums() {
        return this.minimums;
    }

    public float[] getMaximums() {
        return this.maximums;
    }

    public float[] getMeans() {
        return this.means;
    }

    public float[] getVariances() {
        // compute covariance according to online algorithm
        float[] variances = new float[this.dimensionality];
        for (int i=0; i<this.dimensionality; i++) {
            variances[i] = this.m2[i] / this.count;
        }

        return variances;
    }
}
