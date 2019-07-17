package ru.profit.teamcenter.import_tp.app.model.build_tp;

import ru.profit.teamcenter.import_tp.app.model.mapping_config.fox.MappingConfigImpl;
import ru.profit.teamcenter.import_tp.app.model.mapping_config.fox.MappingFromFoxToTpConfig;
import ru.profit.teamcenter.import_tp.app.model.tp.TpConfig;

import java.util.*;

/**
 * Проверяет конфигурации на соответствие предъявляемым ограничениям
 *
 *
 */
public class ConfigurationChecker {

    private String errorMessage;

    public boolean checkConfigurations(MappingFromFoxToTpConfig fromFoxMappingConfig, TpConfig tpConfig){

        if (!checkConfigSupportString(fromFoxMappingConfig)) return false;
        if (!checkConfigAllowedTypes(tpConfig)) return false;
        if (!checkConfigAttributePresence(fromFoxMappingConfig)) return false;
        if (!checkConfigRelationLogic(fromFoxMappingConfig, tpConfig)) return false;

        return true;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    // Если указана вспомогательная строка для какой либо базовой строки,
    // то базовая строка должна переводиться в единственный тип объекта технологии, не несколько.
    private boolean checkConfigSupportString(MappingFromFoxToTpConfig fromFoxMappingConfig){
        if( fromFoxMappingConfig instanceof MappingConfigImpl){
            Map<String, List<String>> associationSignAndType = ((MappingConfigImpl) fromFoxMappingConfig).getAssociationSignAndType();
            Map<String, List<String>> supportingSignsAndAssociation = ((MappingConfigImpl) fromFoxMappingConfig).getSupportingSignsAndAssociation();
            for ( String mainSign : associationSignAndType.keySet()){
                for (List<String> supportSigns : supportingSignsAndAssociation.values()){
                    if (supportSigns.contains(mainSign)){
                        if (associationSignAndType.get(mainSign).size() > 1){
                            errorMessage = "Вспомогательная строка была обнаружена для базовой строки " + mainSign + ". Базовая строка должна переводиться в единственный тип объекта технологии, не несколько.";
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    //Конфигурация модели ТП
    //Есть ограничения: не должно быть типов "CONTAINER_INFO_FOR_SUPPORTING_LINE", "UNKNOWN_TYPE"
    private boolean checkConfigAllowedTypes(TpConfig tpConfig){
        if (tpConfig.getAvailableTypes().contains("CONTAINER_INFO_FOR_SUPPORTING_LINE") || tpConfig.getAvailableTypes().contains("UNKNOWN_TYPE")){
            errorMessage = "В конфигурации модели ТП не должно быть типов \"CONTAINER_INFO_FOR_SUPPORTING_LINE\", \"UNKNOWN_TYPE\"";
            return false;
        }
        return true;
    }

    // Конфигурация модели ТП и конфигурация маппинга должна быть такой, что если в конфиге мапинга из Фокс одному типу строки соответствует несколько типов объектов технологии,
    // в модели не должно быть неоднозначности какой тип в какой входит (по правилу "состоит из").
    // т.е. типы могут не иметь связи между собой "состоит из", но не должно быть (тип1 состоит из тип 2, и тип2 состоит из тип1), а также не должно быть
    // (тип 3 может входить и в тип1 и в тип2).
    private boolean checkConfigRelationLogic(MappingFromFoxToTpConfig fromFoxMappingConfig, TpConfig tpConfig){

        if( fromFoxMappingConfig instanceof MappingConfigImpl){
            Set<Map.Entry<String, List<String>>> entries = ((MappingConfigImpl) fromFoxMappingConfig).getAssociationSignAndType().entrySet();
            for (Map.Entry<String, List<String>> entry: entries){
                List<String> typeList = entry.getValue();
                if (typeList.size() > 1){
                    for (int i = 0; i < typeList.size() - 1; i++){
                        String typeForCheck = typeList.get(i);
                        Set<String> relationsForCheck = tpConfig.getAvailableRelations().get(typeForCheck);
                        if (relationsForCheck != null){
                            boolean foundRelation = false;
                            String firstRelation = "";
                            for (String type : typeList){
                                if (relationsForCheck.contains(type)){
                                    if (!foundRelation){
                                        firstRelation = type;
                                        foundRelation = true;
                                    }else{
                                        errorMessage = "Тип " + typeForCheck + " входит в несколько типов (" + firstRelation + "," + type +") в списке типов, получаемых по строке " + entry.getKey();
                                        return false;
                                    }
                                }
                            }
                            for (int j = i + 1; j < typeList.size(); j++){
                                if (relationsForCheck.contains(typeList.get(j))){
                                    if (tpConfig.getAvailableRelations().get(typeList.get(j)).contains(typeForCheck)) {
                                        errorMessage = "тип " + typeForCheck + " состоит из типа " + typeList.get(j) + " и тип " + typeList.get(j) + " состоит из типа " + typeForCheck;
                                        return false;
                                    }
                                }
                            }

                        }
                    }
                }
            }
        }
        return true;
    }

    private boolean checkConfigAttributePresence (MappingFromFoxToTpConfig fromFoxMappingConfig){
        //проверка на свопадение количества объектов и аттрибутов, описывающих их
        if( fromFoxMappingConfig instanceof MappingConfigImpl){
            Map<String, List<String>> associationSignAndType = ((MappingConfigImpl) fromFoxMappingConfig).getAssociationSignAndType();
            Map<String, List<Map<Integer, String>>> attributesMapping = ((MappingConfigImpl) fromFoxMappingConfig).getAttributesMapping();

            for (Map.Entry<String,List<String>> signAndType : associationSignAndType.entrySet()){
                if (!attributesMapping.containsKey(signAndType.getKey()) || signAndType.getValue().size() != attributesMapping.get(signAndType.getKey()).size()){
                    errorMessage = "Аттрибуты для типов, полученных по строке " + signAndType.getKey() + " не указаны или указаны не полностью.";
                    return false;
                }
            }
        }
        return true;
    }
}
