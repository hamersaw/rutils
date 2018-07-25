package com.bushpath.rutils.sample;

import java.util.Random;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ReserviorSample {
    protected Random random;
    protected double[][] data;
    protected int count;
    protected ReadWriteLock lock;

    public ReserviorSample(int count) {
        this.random = new Random(System.nanoTime());
        this.data = new double[count][];
        this.count = 0;
        this.lock = new ReentrantReadWriteLock();
    }

    public void update(double[] d) throws Exception {
        this.lock.writeLock().lock();
        try {
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
        } finally {
            this.lock.writeLock().unlock();
        }
    }

    public double[][] getData() {
        this.lock.readLock().lock();
        try {
            return this.data;
        } finally {
            this.lock.readLock().unlock();
        }
    }
}
