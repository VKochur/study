package ru.profit.teamcenter.import_tp.app.model.repository;

import org.junit.Test;

import static org.junit.Assert.*;

public class TpContentFoxTest {

    @Test
    public void testContent(){
        TpContent contentExpect = new TpContentImpl();
        TpContent contentActual = new TpContentFox();
        try {
            System.out.println(contentExpect.getContent("C:\\Users\\User\\source\\profconfig\\3741-3802034.DBF"));
            System.out.println("-----------------------");
            System.out.println(contentActual.getContent("C:\\Users\\User\\source\\profconfig\\3741-3802034.DBF"));
            assertEquals(contentExpect.getContent("C:\\Users\\User\\source\\profconfig\\3741-3802034.DBF"),contentActual.getContent("C:\\Users\\User\\source\\profconfig\\3741-3802034.DBF"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}