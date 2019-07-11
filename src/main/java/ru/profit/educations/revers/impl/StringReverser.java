package ru.profit.educations.revers.impl;

import ru.profit.educations.revers.Reverser;

public class StringReverser implements Reverser {
    @Override
    public String revers(String phrase) {
        String[] array = phrase.split(SEPARATOR);
        String result = "";
        for (String word : array){
            char[] chars = word.toCharArray();
            char[] reversChars = new char[chars.length];
            for (int i = 0; i < chars.length; i++){
                reversChars[i] = chars[chars.length - 1 - i];
            }
            result += new String(reversChars) + " ";
        }

        return result;
    }
}
