package ru.nsu.stats;

public interface DataStats<T> {
    void add(T value);
    int getCount();
}
