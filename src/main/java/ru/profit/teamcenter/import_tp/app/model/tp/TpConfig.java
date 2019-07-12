package ru.profit.teamcenter.import_tp.app.model.tp;

import java.util.Map;
import java.util.Set;

public class TpConfig {

    private Map<String, Set<String>> availableAttributes;
    private Map<String, Set<String>> availableRelations;
    private Set<String> availableTypes;

    public TpConfig() {
    }

    /**
     *
     * @param typeElement
     * @return set available names of attributes. Throw IAE if type not correct
     */
    public Set<String> getAllowableAttributes(String typeElement){
        return availableAttributes.get(getAvailableType(typeElement));
    }

    /**
     *
     * @param typeElement
     * @return set available types for parent. Throw IAE if type not correct
     */
    public Set<String> allowableTypesParents(String typeElement){
        return availableRelations.get(getAvailableType(typeElement));
    }

    /**
     *
     * @param typeElement
     * @return
     */
   // public Set<String> allowableTypesChilds(String typeElement){}

    /**
     *
     * @param typeElement
     * @return return typeElement, or throws IAE, if typeElement not available
     */
    public String getAvailableType(String typeElement){
        if (availableTypes.contains(typeElement)) {
            return typeElement;
        } else {
            throw new IllegalArgumentException("Wrong typeElement = " + typeElement + ". Available types is " + availableTypes);
        }
    }


    void setAvailableAttributes(Map<String, Set<String>> availableAttributes) {
        this.availableAttributes = availableAttributes;
    }

    void setAvailableRelations(Map<String, Set<String>> availableRelations) {
        this.availableRelations = availableRelations;
    }

    void setAvailableTypes(Set<String> availableTypes) {
        this.availableTypes = availableTypes;
    }

    public Map<String, Set<String>> getAvailableAttributes() {
        return availableAttributes;
    }

    public Map<String, Set<String>> getAvailableRelations() {
        return availableRelations;
    }

    public Set<String> getAvailableTypes() {
        return availableTypes;
    }
}
