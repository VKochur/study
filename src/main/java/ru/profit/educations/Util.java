package ru.profit.educations;

import ru.profit.educations.entity.Animal;
import ru.profit.educations.entity.Flyable;

import java.util.Scanner;

public class Util {

    public static Long getCurrentPoint(){
        return System.nanoTime();
    }

    /**
     *
     * @return random numeral from [0, 100]
     */
    public static Integer getRandomNumeral(){
        return (int)(java.lang.Math.random()*100);
    }

    //todo implementaion
    public static Integer getRandomNumeral(Integer from, Integer to){
        return 0;
    }

    public static Integer getIntegerFromIn(String message){
        System.out.println(message + ":");
        Scanner scanner = new Scanner(System.in);
        return scanner.nextInt();
    }

    public static String getStringFromIn(String message) {
        System.out.println(message + ":");
        Scanner scanner = new Scanner(System.in);
        return scanner.next();
    }

}
