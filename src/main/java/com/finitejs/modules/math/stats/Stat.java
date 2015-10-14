package com.finitejs.modules.math.stats;

import org.apache.commons.math3.stat.descriptive.SummaryStatistics;

import java.util.Arrays;

public class Stat {

    public static double mean(double[] values) {
        SummaryStatistics stats = new SummaryStatistics();
        Arrays.stream(values).forEach(value -> {
            stats.addValue(value);
        });
        return stats.getMean();
    }

    public static double std(double[] values) {
        SummaryStatistics stats = new SummaryStatistics();
        Arrays.stream(values).forEach(value -> {
            stats.addValue(value);
        });
        return stats.getStandardDeviation();
    }

}
