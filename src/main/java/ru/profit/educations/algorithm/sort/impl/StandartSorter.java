package ru.profit.educations.algorithm.sort.impl;

import ru.profit.educations.algorithm.sort.Sorter;

import java.util.*;

public class StandartSorter<T> implements Sorter<T> {
    public Integer[] sort(Integer[] source) {
        Arrays.sort(source);
        return source;
    }

    public List<? extends T> sort(List<? extends T> source, Comparator<T> comparator) {
        List<? extends T> ordered = new ArrayList<>(source);
        Collections.sort(ordered, comparator);
        return ordered;
    }

    public T[] sort(T[] source, Comparator<T> comparator) {
        T[] sorted = Arrays.copyOf(source, source.length);
        Arrays.sort(sorted);
        return sorted;
    }
}
