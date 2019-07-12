package ru.profit.teamcenter.import_tp.app.model.tp;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

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
        byte[] jsonData = Files.readAllBytes(Paths.get(pathConfigFile));
        ObjectMapper objectMapper = new ObjectMapper();
        TpConfig tpConfig = objectMapper.readValue(jsonData,TpConfig.class);
        return tpConfig;
    }
}
