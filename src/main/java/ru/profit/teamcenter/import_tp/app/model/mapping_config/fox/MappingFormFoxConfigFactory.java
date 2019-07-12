package ru.profit.teamcenter.import_tp.app.model.mapping_config.fox;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class MappingFormFoxConfigFactory {
    public static MappingFromFoxToTpConfig getInstance(String pathConfigFile) throws IOException {
        Parser parser = new Parser();
        parser.processingFile(pathConfigFile);
        MappingConfigImpl mappingConfig = new MappingConfigImpl();
        mappingConfig.setSigns(parser.getSigns());
        mappingConfig.setAssociationSignAndType(parser.getAssociationSignAndType());
        mappingConfig.setAttributesMapping(parser.getAttributesMapping());
        mappingConfig.setSupportingSignsAndAssociation(parser.getSupportingSignsAndAssociationToBasedSigns());
        mappingConfig.setDirectionForSearchBaseLineBySupportingLine(parser.getDirectionForSearchBaseLineBySupportingLine());
        mappingConfig.setSeparators(parser.getSeparators());
        return mappingConfig;
    }

    public static MappingFromFoxToTpConfig getInstanceFromJson(String pathConfigFile) throws IOException {
        byte[] jsonData = Files.readAllBytes(Paths.get(pathConfigFile));
        ObjectMapper objectMapper = new ObjectMapper();
        MappingConfigImpl mappingConfig = objectMapper.readValue(jsonData,MappingConfigImpl.class);
        return mappingConfig;
    }
}
