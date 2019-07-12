package ru.profit.teamcenter.import_tp.app.model.mapping_config.fox;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class Parser {

    private List<String> signs;
    private Map<String, List<String>> associationSignAndType;
    private Map<String, List<Map<Integer, String>>> attributesMapping;
    private Map<String, List<String>> supportingSignsAndAssociationToBasedSigns;
    private Map<String, String> directionForSearchBaseLineBySupportingLine;
    private Map<String, Map<Integer, String>> separators;

    public void processingFile(String path) throws IOException {
        Properties properties = new Properties();
        try (InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream(path), StandardCharsets.UTF_8.name())) {
            properties.load(inputStreamReader);
        } catch (IOException e) {
            throw new IOException("Problem with file = " + new File(path).getAbsolutePath() + ". " + e.getMessage());
        }
        defineSignsAndMappingAttributes(properties.getProperty("ATTRIB_MAPPING"));
        defineAssociationSignAndTypes(properties.getProperty("ASSOCIATION_SIGN_TYPE"));
        defineSupportingSigns(properties.getProperty("SUPPORTING_SIGN"));
        defineSeparators(properties.getProperty("SEPARATORS_INSIDE_ATTRIBS"), properties);
    }

    private void defineSeparators(String aboutSeparators, Properties properties) {
        separators = new HashMap<>();
        String[] bySpecificLines = trim(aboutSeparators).split(";");
        for (String specificLine : bySpecificLines) {
            String[] temp = specificLine.split("=");
            String sign = temp[0];
            Map<Integer,String> mappingSubstringToSeparator = new HashMap<>();
            String[] bySubstrings = temp[1].split("&");
            for (String substring : bySubstrings) {
                String[] temp2 = trim(substring).split(",");
                String separator = properties.getProperty(temp2[1]);
                if (Objects.isNull(separator)) {
                    throw new IllegalStateException("Wrong format properties file (skip separator's value) for line: " + substring);
                } else {
                    mappingSubstringToSeparator.put(Integer.parseInt(temp2[0]), separator);
                }
            }
            separators.put(sign, mappingSubstringToSeparator);
        }
    }

    private void defineSupportingSigns(String supportingSigns) {
        supportingSignsAndAssociationToBasedSigns = new HashMap<>();
        directionForSearchBaseLineBySupportingLine = new HashMap<>();
        String[] signsThatAreSupporting = trim(supportingSigns).split(";");
        for (String strForSpecificSign : signsThatAreSupporting) {
            String[] temp = strForSpecificSign.split("=");
            String supportingSign = temp[0];
            String[] temp2 = trim(temp[1]).split("-");
            directionForSearchBaseLineBySupportingLine.put(supportingSign, temp2[1]);
            String[] arrayBasedSigns = trim(temp2[0]).split(",");
            List<String> basedSigns = Arrays.asList(arrayBasedSigns);
            supportingSignsAndAssociationToBasedSigns.put(supportingSign, basedSigns);
        }
    }

    private void defineAssociationSignAndTypes(String associationSignAndTypes) {
        associationSignAndType = new LinkedHashMap<>();
        String[] associationsForSigns = trim(associationSignAndTypes).split(";");
        for (String association : associationsForSigns) {
            String[] temp = association.split("=");
            String strTypes = trim(temp[1]);
            List<String> types = (Arrays.asList(strTypes.split(",")));
            associationSignAndType.put(temp[0], types);
        }
    }

    private void defineSignsAndMappingAttributes(String attrMapping) {
        signs = new ArrayList<>();
        attributesMapping = new TreeMap<String, List<Map<Integer, String>>>();

        String[] temp = trim(attrMapping).split(";");
        //разбираем различные типы строк
        for (String mappingForSpecificSign : temp) {

            String[] temp2 = mappingForSpecificSign.split("=");
            String specificSign = temp2[0];
            signs.add(specificSign);
            String mapsAttrib = trim(temp2[1]);
            String[] temp3 = mapsAttrib.split("&");

            //разбираем маппинги для различных типов
            List<Map<Integer,String>> listMapingsForTypes = new ArrayList<>();
            for (String attributesForSpecificType : temp3) {
                String[] numberToAttributeNames = trim(attributesForSpecificType).split("\\|");

                //разбираем атрибуты для определенного типа
                Map<Integer, String> mappingNumberSubstringToNameAttribute = new HashMap<>();
                for (String numberToAttributeName : numberToAttributeNames) {
                    String[] mappingNumberToAttribute = numberToAttributeName.split("-");
                    Integer numberSubString = Integer.parseInt(mappingNumberToAttribute[0]);
                    String attributeName = mappingNumberToAttribute[1];
                    mappingNumberSubstringToNameAttribute.put(numberSubString,attributeName);
                }

                listMapingsForTypes.add(mappingNumberSubstringToNameAttribute);
            }

            attributesMapping.put(specificSign, listMapingsForTypes);
        }
    }

    private String trim(String self){
        return self.substring(1, self.length()-1);
    }

    public List<String> getSigns() {
        return signs;
    }

    public Map<String, List<String>> getAssociationSignAndType() {
        return associationSignAndType;
    }

    public Map<String, List<Map<Integer, String>>> getAttributesMapping() {
        return attributesMapping;
    }

    public Map<String, List<String>> getSupportingSignsAndAssociationToBasedSigns() {
        return supportingSignsAndAssociationToBasedSigns;
    }

    public Map<String, String> getDirectionForSearchBaseLineBySupportingLine() {
        return directionForSearchBaseLineBySupportingLine;
    }

    public Map<String, Map<Integer, String>> getSeparators() {
        return separators;
    }
}
