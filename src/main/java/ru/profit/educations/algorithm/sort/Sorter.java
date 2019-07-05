package ru.profit.educations.algorithm.sort;

import java.util.Comparator;
import java.util.List;

public interface Sorter<T> {

    Integer[] sort(Integer[] source);

    List<? extends T> sort(List<? extends T> source, Comparator<T> comparator);

    T[] sort(T[] cource, Comparator<T> comparator);

}
