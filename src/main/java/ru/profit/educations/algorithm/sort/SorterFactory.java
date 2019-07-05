package ru.profit.educations.algorithm.sort;

import ru.profit.educations.algorithm.sort.impl.BubbleSorter;
import ru.profit.educations.algorithm.sort.impl.StandartSorter;

import java.util.Arrays;

public class SorterFactory<T> {
    public Sorter getInstance(SorterType type){
        switch (type) {
            case BUBBLE: return new BubbleSorter<T>();
            case STANDART: return new StandartSorter<T>();
            default: {
                throw new IllegalArgumentException("Wrong type " + type + ". Allowable types: " + getAllowableTypes());
            }
        }
    }

    private static String getAllowableTypes() {
        return Arrays.toString(new SorterType[]{SorterType.BUBBLE, SorterType.STANDART});
    }
}
