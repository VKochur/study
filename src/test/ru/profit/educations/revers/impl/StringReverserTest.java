package ru.profit.educations.revers.impl;

import org.junit.Test;

import static org.junit.Assert.*;

public class StringReverserTest {

    StringReverser reverser = new StringReverser();

    @Test
    public void testRevers() {
        String input = "Compares its two arguments for order. Returns a negative integer, zero, or a positive integer as the first argument is less than, equal to, or greater than the second.";
        String expect = "serapmoC sti owt stnemugra rof .redro snruteR a evitagen ,regetni ,orez ro a evitisop regetni sa eht tsrif tnemugra si ssel ,naht lauqe ,ot ro retaerg naht eht .dnoces";

        assertEquals(expect, reverser.revers(input));
    }

    @Test
    public void testRevers1(){
        String input = "";

        assertEquals("", reverser.revers(input));
    }

    @Test (expected = NullPointerException.class)
    public void testRevers2(){
        reverser.revers(null);
    }

    @Test
    public void testRevers3() {
        String input = "  asd   mj 332 kjh  mj   ";
        String expect = "  dsa   jm 233 hjk  jm   ";

        assertEquals(expect, reverser.revers(input));
    }
}