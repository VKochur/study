package ru.profit.educations;

import java.util.*;

public class Wardrobes {
    public static List<List<String>> wardrobes = Arrays.asList(
            Arrays.asList("Shoes1", "Shoes2", "Shoes3", "Shoes4"),
            Arrays.asList("Pants1", "Pants2"),
            Arrays.asList("Hat1", "Hat2", "Hat3")
    );

    public static List<String> calculate(List<List<String>> inputList){
        //если передан спиок с единственным элементом, возвращаем этот элемент
        if (inputList.size() == 1){
            return inputList.get(0);
        }else{
            //последний элемент ("хвост") исходного списка
            List<String> tail = inputList.get(inputList.size() - 1);
            //остальные элементы ("тело") исходного списка
            List<List<String>> body = inputList.subList(0,inputList.size() - 1);
            //индекс последнего элемента "тела"
            int bodyLastIndex = body.size() - 1;

            List<String> combos = new ArrayList<>();
            //перебор элементов последнего элемента "тела"
            for (String comboBody : body.get(bodyLastIndex)){
                //перебор элементов "хвоста"
                for (String comboTail : tail){
                    combos.add(comboBody + comboTail);
                }
            }
            //заменяем последний элемент "тела" его комбинациями с "хвостом"
            body.set(bodyLastIndex, combos);

            return calculate(body);
        }
    }
}
