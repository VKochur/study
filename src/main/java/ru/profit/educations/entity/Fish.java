package ru.profit.educations.entity;

public class Fish extends Animal implements Swimmer {

    @Override
    public void swim() {
        System.out.println(this + " swim");
    }
}
