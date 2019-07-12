package ru.profit.teamcenter.import_tp.app.model.mapping_config.fox;

import org.junit.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Paths;

import static org.junit.Assert.*;

public class MappingFormFoxConfigFactoryTest {

    @Test
    public void getInstance() {
        try {
            String pathProperty = Paths.get(getClass().getClassLoader().getResource("configMappingFox.properties").toURI()).toString();
            String pathJson = Paths.get(getClass().getClassLoader().getResource("configMappingFox.json").toURI()).toString();

            MappingConfigImpl instance = (MappingConfigImpl) MappingFormFoxConfigFactory.getInstance(pathProperty);
            MappingConfigImpl instanceJson = (MappingConfigImpl) MappingFormFoxConfigFactory.getInstanceFromJson(pathJson);

            assertEquals(instance.getValidSigns(),instanceJson.getValidSigns());
            assertEquals(instance.getAssociationSignAndType(),instanceJson.getAssociationSignAndType());
            assertEquals(instance.getAttributesMapping(),instanceJson.getAttributesMapping());
            assertEquals(instance.getSupportingSignsAndAssociation(),instanceJson.getSupportingSignsAndAssociation());
            assertEquals(instance.getDirectionForSearchBaseLineBySupportingLine(),instanceJson.getDirectionForSearchBaseLineBySupportingLine());
            assertEquals(instance.getSeparators(),instanceJson.getSeparators());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }
}