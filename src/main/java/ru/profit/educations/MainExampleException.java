package ru.profit.educations;

import ru.profit.educations.entity.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedHashSet;
import java.util.Set;

public class MainExampleException {

    public static void main(String[] args) {
        System.out.println("---------------0------------------");
        sendToFly(null);
        System.out.println("---------------1------------------");
        checkedSendToFly(new Bird());
        System.out.println("----------------2-----------------");

        try {
            cheсkedSendToFlyReflection(new Bird());
        } catch (NoSuchMethodException e) {
            System.out.println(e.getMessage());
        } catch (InvocationTargetException e) {
            System.out.println(e.getMessage());
        } catch (IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
        System.out.println("----------------3-----------------");

        try {
            cheсkedSendToFlyReflection(new Fish());
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            System.out.println(e.getClass() + " " + e.getMessage());
        }

        System.out.println("----------------4-----------------");

        Set<Animal> animals = new LinkedHashSet<>();
        animals.add(new Cat());
        animals.add(new Bird());
        animals.add(new Fish());

        fly(animals);

    }

    public static void sendToFly(Animal animal) {
        try {
            ((Flyable) animal).fly();
        } catch (ClassCastException e) {
            System.out.println("не правильное применение типа");
        } catch (NullPointerException e) {
            System.out.println("нет инициализации");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            System.out.println("код выполняется всегда");
        }
    }

    public static void checkedSendToFly(Animal animal) {
        if (animal instanceof Flyable) {
            ((Flyable) animal).fly();
            System.out.println("улетели");
        } else {
            throw new IllegalArgumentException(animal + " не летает");
        }
    }

    public static void cheсkedSendToFlyReflection(Animal animal) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Class<? extends Animal> animalClass = animal.getClass();
        Method fly = animalClass.getMethod("fly");
        fly.invoke(animal);
    }

    public static void fly(Animal animal) throws MyCheсkedException {
        try {
            checkedSendToFly(animal);
        } catch (IllegalArgumentException e) {
            throw new MyCheсkedException(e.getMessage());
        }
    }

    public static void fly(Set<Animal> animals) {
        for (Animal animal : animals) {
            try {
                fly(animal);
            } catch (MyCheсkedException e) {
                System.out.println(e.getMessage());
            }
        }
    }

}
