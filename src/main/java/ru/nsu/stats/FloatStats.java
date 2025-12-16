package ru.nsu.stats;

public class FloatStats implements DataStats<Float> {
    private float maxValue;
    private float minValue;
    private int count;
    private double sum;

    public FloatStats() {
        maxValue = Float.MIN_VALUE;
        minValue = Float.MAX_VALUE;
        count = 0;
        sum = 0;
    }

    // Adds a value to the statistics
    public void add(Float value) {
        count++;
        sum += value;
        if (value > maxValue) {
            maxValue = value;
        }
        if (value < minValue) {
            minValue = value;
        }
    }

    public double getAverage() {
        if (count == 0) {
            return 0;
        }
        else {
            return (double) sum / count;
        }
    }

    public float getMaxValue() { return maxValue; }
    public float getMinValue() { return minValue; }
    public int getCount() { return count; }
    public double getSum() { return sum; }
}
