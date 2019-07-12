package ru.profit.teamcenter.import_tp.app.model.mapping_config.fox;

/**
 * В случае обнаружения некорректного формата конфигурации
 */
public class IllegalFormatConfiguration extends RuntimeException{
    public IllegalFormatConfiguration(String message) {
        super(message);
    }
}
