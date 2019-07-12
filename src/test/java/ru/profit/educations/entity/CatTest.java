package ru.profit.educations.entity;

import org.junit.Test;
import static org.junit.Assert.*;

public class CatTest {

    @Test
    public void testSpeak() {
        String expected = Cat.class + " Tom speaks: hello";
        assertEquals(expected, new Cat("Tom").speak("hello"));
    }

    @Test (expected = NullPointerException.class)
    public void testSpeak2(){
        Cat cat = null;
        assertEquals("",cat.speak("something"));
    }

}