package ru.profit.teamcenter.import_tp.app.model.tp;

import org.junit.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Paths;

import static org.junit.Assert.*;

public class TpConfigFactoryTest {

    @Test
    public void getInstance() {
        try {
            String path1 = Paths.get(getClass().getClassLoader().getResource("configTp.json").toURI()).toString();
            String path2 = Paths.get(getClass().getClassLoader().getResource("configTp.properties").toURI()).toString();

            TpConfig instance = TpConfigFactory.getInstance(path2);
            TpConfig instanceJson = TpConfigFactory.getInstanceFromJson(path1);

            System.out.println(instance.getAvailableTypes());
            System.out.println(instance.getAvailableAttributes());
            System.out.println(instance.getAvailableRelations());

            System.out.println("--------------");

            System.out.println(instanceJson.getAvailableTypes());
            System.out.println(instanceJson.getAvailableAttributes());
            System.out.println(instanceJson.getAvailableRelations());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }
}