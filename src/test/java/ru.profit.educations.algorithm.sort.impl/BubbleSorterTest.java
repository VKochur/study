package ru.profit.educations.algorithm.sort.impl;

import org.junit.Test;
import ru.profit.educations.algorithm.sort.Sorter;
import ru.profit.educations.algorithm.sort.SorterFactory;
import ru.profit.educations.algorithm.sort.SorterType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static org.junit.Assert.*;

public class BubbleSorterTest {

    SorterFactory factory = new SorterFactory();
    Sorter sorter = factory.getInstance(SorterType.BUBBLE);

    @org.junit.Test
    public void testSort() {
        Integer[] inputArray = {-2,4,1,0,1,23,7,-4,2};
        Integer[] expectedArray = {23,7,4,2,1,1,0,-2,-4};

        assertArrayEquals(sorter.sort(inputArray),expectedArray);
    }


    @org.junit.Test (expected = NullPointerException.class)
    public void testSort1() {
        assertArrayEquals(sorter.sort(null),null);
    }

    @org.junit.Test
    public void testSort2() {
        String[] inputArray = {"qwe","asd","aaaaaa","qywdbudqbw","12","","'"};
        String[] expectedArray = {"qywdbudqbw","aaaaaa","qwe","asd","12","'",""};

        assertArrayEquals(sorter.sort(inputArray, (Comparator<String>) (first, last) -> first.length()-last.length()),expectedArray);
    }

    @Test
    public void setSort3(){
        List<Integer> inputList = new ArrayList<Integer>();
        inputList.add(3);
        inputList.add(-4);
        inputList.add(1);
        inputList.add(11);
        inputList.add(0);
        inputList.add(3);
        inputList.add(4);
        inputList.add(-4);
        inputList.add(7);
        inputList.add(2);

        Collections.sort(inputList);
        Collections.reverse(inputList);

        assertEquals(inputList, sorter.sort(inputList,(Comparator<Integer>) (first,last) -> first.compareTo(last)));
    }

    @Test
    public void setSort4(){
        List<Integer> inputList = new ArrayList<>();


        Collections.sort(inputList);
        Collections.reverse(inputList);

        assertEquals(inputList, sorter.sort(inputList,(Comparator<Integer>) (first,last) -> first.compareTo(last)));
    }
}