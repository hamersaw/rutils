package com.bushpath.rutils.quantize;

import de.tuhh.luethke.okde.model.SampleModel;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.integration.SimpsonIntegrator;
import org.ejml.simple.SimpleMatrix;

public class OnlineKernelDensityEstimator implements UnivariateFunction {
    protected static final double DEFAULT_FORGET = 1;
    protected static final double DEFAULT_COMPRESSION = 0.02;
    protected static final double DEFAULT_WEIGHT = 1;
    protected static final SimpleMatrix UNIT_MATRIX = new SimpleMatrix(1, 1);

    protected SampleModel sampleModel;
    protected double minimum;
    protected double maximum;

    public OnlineKernelDensityEstimator() {
        this.sampleModel = new SampleModel(DEFAULT_FORGET, DEFAULT_COMPRESSION);
        this.minimum = Double.MAX_VALUE;
        this.maximum = Double.MIN_VALUE;
    }

    public OnlineKernelDensityEstimator(double forget, double compression) {
        this.sampleModel = new SampleModel(forget, compression);
        this.minimum = Double.MAX_VALUE;
        this.maximum = Double.MIN_VALUE;
    }

    public void initialize(double[] samples) throws Exception {
        // initialize matrices
        SimpleMatrix[] sampleMatrices = new SimpleMatrix[samples.length];
        SimpleMatrix[] covarianceMatrices = new SimpleMatrix[samples.length];
        double[] weights = new double[samples.length];

        for (int i=0; i<samples.length; i++) {
            sampleMatrices[i] = new SimpleMatrix(new double[][] { { samples[i] } });
            covarianceMatrices[i] = UNIT_MATRIX;
            weights[i] = DEFAULT_WEIGHT;

            // update minimum and maximum
            if (this.minimum > samples[i]) {
                this.minimum = samples[i];
            }

            if (this.maximum < samples[i]) {
                this.maximum = samples[i];
            }
        }

        // update model
        this.sampleModel.updateDistribution(sampleMatrices, covarianceMatrices, weights);
    }

    public void update(double sample) throws Exception {
        // initialize sample matrix
        SimpleMatrix sampleMatrix = new SimpleMatrix(new double[][]{{sample}});

        // update model
        this.sampleModel.updateDistribution(sampleMatrix, UNIT_MATRIX, DEFAULT_WEIGHT);
 
        // update minimum and maximum
        if (this.minimum > sample) {
            this.minimum = sample;
        }

        if (this.maximum < sample) {
            this.maximum = sample;
        }
    }

    public double[] computeBinBoundaries(int binCount, double stepPercent,
            double stepBackoff, double integralLeniencyPercent) {
        SimpsonIntegrator integrator = new SimpsonIntegrator();

        // initialize varialbes
        double[] boundaries = new double[binCount - 1];
        double stepValue = (this.maximum - this.minimum) * stepPercent;
        double targetIntegralPercent = 1.0 / binCount;

        // compute boundaries
        double startValue;
        double endValue = minimum; // initialize to minimum for first loop
        for (int i=0; i<boundaries.length; i++) {
            System.out.println("computing boundary " + i);
            // initialize start and end values
            startValue = endValue;
            endValue = startValue + stepValue;

            // modify endValue until integralPercent ~= tickIntegralPercent
            while (true) {
                // check if integralPercent ~= targetIntegralPercent
                double integralPercent =
                    integrator.integrate(Integer.MAX_VALUE, this, startValue, endValue);
                if (integralLeniencyPercent > Math.abs(integralPercent 
                        - targetIntegralPercent) / targetIntegralPercent) {
                    break;
                }

                // add / subtract endValue to increase / decrease integralPercent
                if (integralPercent < targetIntegralPercent) {
                    endValue += stepValue;
                } else {
                    endValue -= stepValue * stepBackoff;
                }
            }

            // add boundary
            boundaries[i] = endValue;
        }

        return boundaries;
    }

    @Override
    public double value(double value) {
        return this.sampleModel.evaluate(new SimpleMatrix(new double[][]{{value}}));
    }

    public double getMinimum() {
        return this.minimum;
    }

    public double getMaximum() {
        return this.maximum;
    }
}
