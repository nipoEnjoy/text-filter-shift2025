package ru.nsu.stats;

public class IntegerStats implements DataStats<Integer> {
    private int maxValue;
    private int minValue;
    private int count;
    private long sum;

    public IntegerStats() {
        maxValue = Integer.MIN_VALUE;
        minValue = Integer.MAX_VALUE;
        count = 0;
        sum = 0;
    }

    // Adds a value to the statistics
    public void add(Integer value) {
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

    public int getMaxValue() { return maxValue; }
    public int getMinValue() { return minValue; }
    public int getCount() { return count; }
    public long getSum() { return sum; }
}
