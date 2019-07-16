package ru.profit.teamcenter.import_tp.app.model.build_tp;

public class MixerFactory {

    /**
     * Фабрика миксеров.
     * Позволяет изменить логику смешивания атрибутов добавив реализацию миксера и указав классу соответствующее название.
     * Чтобы изменить логику смешивания для Базовой строки "O" и вспомогательной "o" следует добавить
     * реализацию Mixer ru.profit.teamcenter.import_tp.app.model.build_tp.mixers.O_o
     * @param mixerName
     * @return возвращает экзепляр класса ru.profit.teamcenter.import_tp.app.model.build_tp.mixers.mixerName
     * Если не находит такой класс, возвращает экземпляр MixerImplDefault
     */
    public static Mixer getInstance(String mixerName) throws ClassNotFoundException, IllegalAccessException, InstantiationException {

        String packageName = "ru.profit.teamcenter.import_tp.app.model.build_tp.mixers";
        String clazzName = packageName + "." + mixerName;
        Class clazz;

        try {
            clazz = Class.forName(clazzName);
            return (Mixer) clazz.newInstance();
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
            clazzName = "ru.profit.teamcenter.import_tp.app.model.build_tp.mixers.MixerImplDefault";
            clazz = Class.forName(clazzName);
            return (Mixer) clazz.newInstance();
        }
    }
}
