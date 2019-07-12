package ru.profit.educations.entity;

public class Bird extends Animal implements Flyable {

    @Override
    public void fly() {
        System.out.println(this + " fly");
    }
}
