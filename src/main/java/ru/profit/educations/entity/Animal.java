package ru.profit.educations.entity;

import java.util.Objects;
import java.util.UUID;

public abstract class Animal {

    private String id;
    private double weight;

    public Animal() {
        id = UUID.randomUUID().toString();
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public String getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Animal animal = (Animal) o;
        return id.equals(animal.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Animal{" +
                getClass() + " "
                +
                "id='" + id + '\'' +
                ", weight=" + weight +
                '}';
    }
}
