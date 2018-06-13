package com.bushpath.rutils.statistics;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class OnlineStatisticsTest {
    @Test
    public void numberCheck() {
        try {
            // initialize online statistics
            OnlineStatistics stats = new OnlineStatistics();

            stats.update(new double[]{-1,0,1});
            stats.update(new double[]{0,1,2});
            stats.update(new double[]{1,2,3});

            // test values
            double[] minimums = stats.getMinimums();
            for (int i=0; i<minimums.length; i++) {
                assert(minimums[i] == (i - 1));
            }

            double[] maximums = stats.getMaximums();
            for (int i=0; i<maximums.length; i++) {
                assert(maximums[i] == (i + 1));
            }

            double[] means = stats.getMeans();
            for (int i=0; i<means.length; i++) {
                assert(means[i] == i);
            }

            double[] variances = stats.getVariances();
            for (int i=0; i<variances.length; i++) {
                assert(variances[i] == (2.0 / 3.0));
            }
        } catch (Exception e) {

        }
    }
}
