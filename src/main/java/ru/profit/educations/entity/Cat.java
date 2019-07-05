package ru.profit.educations.entity;

public class Cat extends Animal {

    private int age;

    public Cat() {
    }

    public Cat(String name) {
        this.name = name;
    }

    private String name;

    public String speak(String message){
        return String.format("%s %s %s", getClass(), name, "speaks: " + message);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return super.toString() + "Cat{" +
                "age=" + age +
                ", name='" + name + '\'' +
                '}';
    }
}
