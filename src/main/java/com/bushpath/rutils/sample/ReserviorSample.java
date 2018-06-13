package com.bushpath.rutils.sample;

import java.util.Random;

public class ReserviorSample {
    protected Random random;
    protected double[][] data;
    protected int count;

    public ReserviorSample(int count) {
        this.random = new Random(System.nanoTime());
        this.data = new double[count][];
        this.count = 0;
    }

    public void update(double[] d) throws Exception {
        if (this.count < data.length) {
            // if data is not full
            this.data[this.count] = d;
        } else {
            // adopt value into data with probability data.length / this.count
            if (random.nextDouble() <= (this.data.length / (this.count + 1))) {
                // choose random item to replace
                this.data[random.nextInt(this.data.length)] = d;
            }
        }

        this.count += 1;
    }

    public double[][] getData() {
        return this.data;
    }
}
