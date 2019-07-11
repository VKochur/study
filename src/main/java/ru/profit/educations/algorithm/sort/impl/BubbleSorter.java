package ru.profit.educations.algorithm.sort.impl;

import ru.profit.educations.algorithm.sort.Sorter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

//todo implementation
public class BubbleSorter<T> implements Sorter<T> {

    public Integer[] sort(Integer[] source) {
        Integer [] result = source.clone();

        for (int i = 0; i < result.length - 1; i++){
            for (int j = i + 1; j < result.length; j++){
                if (result[i] < result[j]){
                    Integer temp = result[i];
                    result[i] = result[j];
                    result[j] = temp;
                }
            }
        }
        return result;
    }

    public List<? extends T> sort(List<? extends T> source, Comparator<T> comparator) {
        List<? extends T> result = new ArrayList<>(source);

        for (int i = 0; i < result.size() - 1; i++){
            for (int j = i + 1; j < result.size(); j++){
                  if (comparator.compare(result.get(i), result.get(j)) < 0){
                      Collections.swap(result,i,j);
                }
            }
        }

        return result;
    }

    public T[] sort(T[] source, Comparator<T> comparator) {
        T[] result = source.clone();

        for (int i = 0; i < result.length - 1; i++){
            for (int j = i + 1; j < result.length; j++){
                if (comparator.compare(result[i],result[j]) < 0){
                    T temp = result[i];
                    result[i] = result[j];
                    result[j] = temp;
                }
            }
        }
        return result;
    }
}
