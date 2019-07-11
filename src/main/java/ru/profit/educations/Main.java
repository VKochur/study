package ru.profit.educations;

import ru.profit.educations.algorithm.sort.Sorter;
import ru.profit.educations.algorithm.sort.SorterFactory;
import ru.profit.educations.algorithm.sort.SorterType;
import ru.profit.educations.entity.Cat;
import ru.profit.educations.revers.impl.StringReverser;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        doSortExample();
        doReversExample();
    }

    private static void doReversExample() {
        final String TEXT = "Мама мыла раму, а папа курил трубку! Потом мама спросила: \"И долго ты будешь курить?!\" \n \"Бросаю...\" -лениво ответил отец. Затем пересел на диван, включил футбол.";
        final String TEXT2 = "     Однажды, в студенную зимнюю пору, я из лесу вышел. Был сильный мороз. ";

        StringReverser reverser = new StringReverser();
        System.out.println(TEXT);
        System.out.println(reverser.revers(TEXT));
        System.out.println();
        System.out.println(TEXT2);
        System.out.println(reverser.revers(TEXT2));
    }

    private static void doSortExample() {
        List<Cat> cats = DataHouse.getCats(10);
        System.out.println("Сгенерированные коты:");
        showAnimals(cats);
        List<Cat> orderedCats = doSort(cats, DataHouse.comparatorByName());
        System.out.println("Как изменился первоначальный список:");
        showAnimals(cats);
        System.out.println("Список выданный сортировщиком:");
        showAnimals(orderedCats);
    }

    private static List<Cat> doSort(List<Cat> cats, Comparator<Cat> comparator) {
        List<Cat> sortedCats = null;
        SorterFactory sorterFactory = new SorterFactory();
        String sorterType = Util.getStringFromIn("Укажите тип сортировки. " + Arrays.toString(SorterType.values()));
        try {
            Sorter sorter = sorterFactory.getInstance(SorterType.valueOf(sorterType));
            Long begin = Util.getCurrentPoint();
            sortedCats = sorter.sort(cats, comparator);
            System.out.println("Длительность сортировки " + (Util.getCurrentPoint() - begin));
        } catch (IllegalArgumentException e) {
            System.out.println("from IAE " + e.getMessage());
        } catch (Exception e) {
            System.out.println("from E "+ e.getMessage());
        } finally {
            if (sortedCats == null) {
                sortedCats = Collections.emptyList();
            }
            System.out.println("Блок finally");
        }
        return sortedCats;
    }

    private static void showAnimals(List<Cat> cats) {
        for (int i = 0; i < cats.size(); i++) {
            System.out.println(i + ": " + cats.get(i));
        }
    }
}
