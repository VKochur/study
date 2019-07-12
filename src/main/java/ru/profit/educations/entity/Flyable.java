package ru.profit.educations.entity;

public interface Flyable {

    default void fly(){
        System.out.println("fly");
    }
}
