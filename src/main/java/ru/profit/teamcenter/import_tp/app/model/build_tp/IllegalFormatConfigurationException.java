package ru.profit.teamcenter.import_tp.app.model.build_tp;

/**
 * В случае обнаружения некорректного формата конфигурации
 */
public class IllegalFormatConfigurationException extends Exception{
    public IllegalFormatConfigurationException(String message) {
        super(message);
    }
}
