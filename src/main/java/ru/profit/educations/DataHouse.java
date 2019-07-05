package ru.profit.educations;

import ru.profit.educations.entity.Animal;
import ru.profit.educations.entity.Cat;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class DataHouse {

    static List<Cat> getCats(int count) {
        List<Cat> cats = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            Cat cat = new Cat("Cat" + Util.getRandomNumeral());
            cat.setAge(Util.getRandomNumeral());
            cat.setWeight(Util.getRandomNumeral());
            cats.add(cat);
        }
        return cats;
    }

    static Comparator<? extends Animal> comparableById() {
        return new Comparator<Animal>() {
            @Override
            public int compare(Animal animal, Animal otherAnimal) {
                return animal.getId().compareTo(otherAnimal.getId());
            }
        };
    }

    static Comparator<Cat> comparatorByName() {
        return (cat, otherCat) -> {
            return cat.getName().compareTo(otherCat.getName());
        };
    }

    static Comparator<Cat> comparatorAge(){
        return Comparator.comparingInt(Cat::getAge);
    }

}
