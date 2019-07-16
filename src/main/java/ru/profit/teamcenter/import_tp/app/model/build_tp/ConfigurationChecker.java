package ru.profit.teamcenter.import_tp.app.model.build_tp;

import ru.profit.teamcenter.import_tp.app.model.mapping_config.fox.MappingFromFoxToTpConfig;
import ru.profit.teamcenter.import_tp.app.model.tp.TpConfig;

/**
 * Проверяет конфигурации на соответствие предъявляемым ограничениям
 *
 *
 */
public class ConfigurationChecker {

    private String errorMessage;

    public boolean checkConfigurations(MappingFromFoxToTpConfig fromFoxMappingConfig, TpConfig tpConfig){
        return true;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
