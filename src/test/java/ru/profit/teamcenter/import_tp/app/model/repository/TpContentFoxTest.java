package ru.profit.teamcenter.import_tp.app.model.repository;

import org.junit.Test;

import java.nio.file.Paths;

import static org.junit.Assert.*;

public class TpContentFoxTest {

    @Test
    public void testContent(){
        TpContent contentExpect = new TpContentImpl();
        TpContent contentActual = new TpContentFox();

        try {
            String path = Paths.get(getClass().getClassLoader().getResource("3741-3802034.DBF").toURI()).toString();
            System.out.println(contentExpect.getContent(path));
            System.out.println("-----------------------");
            System.out.println(contentActual.getContent(path));
            assertEquals(contentExpect.getContent(path),contentActual.getContent(path));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}