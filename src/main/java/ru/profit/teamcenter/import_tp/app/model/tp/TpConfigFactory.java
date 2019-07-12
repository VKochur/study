package ru.profit.teamcenter.import_tp.app.model.tp;

import java.io.IOException;

public class TpConfigFactory {
    public static TpConfig getInstance(String pathConfigFile) throws IOException {
        Parser parser = new Parser();
        parser.processingFile(pathConfigFile);
        TpConfig tpConfig = new TpConfig();
        tpConfig.setAvailableTypes(parser.getTypes());
        tpConfig.setAvailableAttributes(parser.getAttributes());
        tpConfig.setAvailableRelations(parser.getRelations());
        return tpConfig;
    }

    public static TpConfig getInstanceFromJson(String pathConfigFile) throws IOException{
        return null;
    }
}
