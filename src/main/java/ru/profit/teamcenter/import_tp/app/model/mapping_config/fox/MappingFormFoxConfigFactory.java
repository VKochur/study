package ru.profit.teamcenter.import_tp.app.model.mapping_config.fox;

import java.io.IOException;

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
}
