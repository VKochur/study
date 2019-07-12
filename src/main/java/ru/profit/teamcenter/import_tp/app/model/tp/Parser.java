package ru.profit.teamcenter.import_tp.app.model.tp;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class Parser {

    private Set<String> types;
    private Map<String, Set<String>> attributes;
    private Map<String, Set<String>> relations;

    public void processingFile(String path) throws IOException {
        Properties properties = new Properties();
        try (InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream(path), StandardCharsets.UTF_8.name())) {
            properties.load(inputStreamReader);
        } catch (IOException e) {
            throw new IOException("Problem with file = " + new File(path).getAbsolutePath() + ". " + e.getMessage());
        }
        defineTypes(properties.getProperty("TYPES"));
        defineAttributes(properties.getProperty("ATTRIBUTES"));
        defineRelations(properties.getProperty("RELATIONS"));
    }

    private void defineAttributes(String strAttributes) {
        attributes = parseMap(strAttributes);
    }

    private void defineRelations(String strRelations) {
        relations = parseMap(strRelations);
    }

    private Map<String, Set<String>> parseMap(String strAttributes) {
        strAttributes = trim(strAttributes);
        Map<String, Set<String>> result = new HashMap<>();
        String[] attrs = strAttributes.split(";");
        for (String attr : attrs) {
            String[] temp = attr.split("=");
            String strNames = trim(temp[1]);
            Set<String> attrNames = new HashSet<>(Arrays.asList(strNames.split(",")));
            result.put(temp[0], attrNames);
        }
        return result;
    }

    private void defineTypes(String strTypes) {
        strTypes = trim(strTypes);
        types = new HashSet<>(Arrays.asList(strTypes.split(",")));
    }

    private String trim(String self){
        return self.substring(1, self.length()-1);
    }

    public Set<String> getTypes() {
        return types;
    }

    public Map<String, Set<String>> getAttributes() {
        return attributes;
    }

    public Map<String, Set<String>> getRelations() {
        return relations;
    }
}
