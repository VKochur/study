package ru.profit.educations;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class WardrobesTest {

    @Test
    public void testCalculate() {
        List<String> expected = Arrays.asList(
                "Shoes1Pants1Hat1", "Shoes1Pants1Hat2", "Shoes1Pants1Hat3",
                "Shoes1Pants2Hat1", "Shoes1Pants2Hat2", "Shoes1Pants2Hat3",
                "Shoes2Pants1Hat1", "Shoes2Pants1Hat2", "Shoes2Pants1Hat3",
                "Shoes2Pants2Hat1", "Shoes2Pants2Hat2", "Shoes2Pants2Hat3",
                "Shoes3Pants1Hat1", "Shoes3Pants1Hat2", "Shoes3Pants1Hat3",
                "Shoes3Pants2Hat1", "Shoes3Pants2Hat2", "Shoes3Pants2Hat3",
                "Shoes4Pants1Hat1", "Shoes4Pants1Hat2", "Shoes4Pants1Hat3",
                "Shoes4Pants2Hat1", "Shoes4Pants2Hat2", "Shoes4Pants2Hat3"
        );
        assertEquals(expected,Wardrobes.calculate(Wardrobes.wardrobes));
    }
}