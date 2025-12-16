package ru.nsu.stats;

public class StringStats implements DataStats<String> {
    private int maxLength;
    private int minLength;
    private int count;

    public StringStats() {
        maxLength = Integer.MIN_VALUE;
        minLength = Integer.MAX_VALUE;
        count = 0;
    }

    // Adds a value to the statistics
    public void add(String value) {
        if (value == null) {
            throw new IllegalArgumentException("String value cannot be null");
        }
        count++;
        if (value.length() > maxLength) {
            maxLength = value.length();
        }
        if (value.length() < minLength) {
            minLength = value.length();
        }
    }

    public int getMaxLength() { return maxLength; }
    public int getMinLength() { return minLength; }
    public int getCount() { return count; }
}
