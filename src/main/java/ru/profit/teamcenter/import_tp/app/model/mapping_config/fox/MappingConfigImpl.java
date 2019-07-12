package ru.profit.teamcenter.import_tp.app.model.mapping_config.fox;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;


public class MappingConfigImpl implements MappingFromFoxToTpConfig {

    private List<String> signs;
    private Map<String, List<String>> associationSignAndType;
    private Map<String, List<Map<Integer, String>>> attributesMapping;
    private Map<String, List<String>> supportingSignsAndAssociation;
    private Map<String, String> directionForSearchBaseLineBySupportingLine;
    private Map<String, Map<Integer, String>> separators;


    @Override
    public List<String> defineType(String sign) throws NoSuchElementException{
        List<String> types = associationSignAndType.get(defineSign(sign));
        if (Objects.nonNull(types)) {
            return types;
        } else {
            throw new NoSuchElementException("LineSign = " + sign + " have not association with types");
        }
    }

    private String defineSign(String sign) {
        if (getValidSigns().contains(sign)){
            return sign;
        } else {
            throw new IllegalArgumentException("Wrong sing = " + sign + ". Valid sign is " + getValidSigns());
        }
    }

    @Override
    public List<String> getValidSigns() {
        return signs;
    }

    @Override
    public List<Map<Integer, String>> getNumberSubstringAndAttribute(String sign) {
        return attributesMapping.get(defineSign(sign));
    }

    @Override
    public List<String> getSignsOfBasedLines(String signOfSupportingLine) {
        return supportingSignsAndAssociation.get(defineSign(signOfSupportingLine));
    }

    @Override
    public Boolean goUpForDefineBaseString(String signOfSupportingLine) {
        String direction = directionForSearchBaseLineBySupportingLine.get(defineSign(signOfSupportingLine));
        if (direction.equals("UP")) {
            return true;
        }
        if (direction.equals("DOWN")){
            return false;
        }
        throw new IllegalArgumentException("For sign " + signOfSupportingLine + " not defined direction for search based line");
    }

    @Override
    public Map<String, Map<Integer, String>> getSeparatorsForSubstringsInLines() {
        return separators;
    }

    @Override
    public String getSeparatorForValuesInSubstringSpecificLine(String signOfLine, Integer numberOfSubstring) throws NoSuchElementException {
        String separator;
        Map<Integer, String> integerStringMap = separators.get(defineSign(signOfLine));
        if (integerStringMap != null) {
            separator = integerStringMap.get(numberOfSubstring);
            if (Objects.nonNull(separator)) {
                return separator;
            }
        }
        throw new NoSuchElementException("For signOfLine = " + signOfLine + " numberSubstring = " + numberOfSubstring
                + " not defined value's separator");
    }

    public void setSigns(List<String> signs) {
        this.signs = signs;
    }

    public void setAssociationSignAndType(Map<String, List<String>> associationSignAndType) {
        this.associationSignAndType = associationSignAndType;
    }

    public void setAttributesMapping(Map<String, List<Map<Integer, String>>> attributesMapping) {
        this.attributesMapping = attributesMapping;
    }

    public void setSupportingSignsAndAssociation(Map<String, List<String>> supportingSignsAndAssociation) {
        this.supportingSignsAndAssociation = supportingSignsAndAssociation;
    }

    public void setDirectionForSearchBaseLineBySupportingLine(Map<String, String> directionForSearchBaseLineBySupportingLine) {
        this.directionForSearchBaseLineBySupportingLine = directionForSearchBaseLineBySupportingLine;
    }

    public Map<String, List<String>> getAssociationSignAndType() {
        return associationSignAndType;
    }

    public Map<String, List<Map<Integer, String>>> getAttributesMapping() {
        return attributesMapping;
    }

    public Map<String, List<String>> getSupportingSignsAndAssociation() {
        return supportingSignsAndAssociation;
    }

    public Map<String, String> getDirectionForSearchBaseLineBySupportingLine() {
        return directionForSearchBaseLineBySupportingLine;
    }

    public Map<String, Map<Integer, String>> getSeparators() {
        return separators;
    }

    public void setSeparators(Map<String, Map<Integer, String>> separators) {
        this.separators = separators;
    }
}
