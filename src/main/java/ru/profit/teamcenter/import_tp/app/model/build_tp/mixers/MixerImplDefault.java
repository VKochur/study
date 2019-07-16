package ru.profit.teamcenter.import_tp.app.model.build_tp.mixers;

import ru.profit.teamcenter.import_tp.app.model.build_tp.Mixer;
import ru.profit.teamcenter.import_tp.app.model.tp.BasicElement;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MixerImplDefault implements Mixer {

    public MixerImplDefault() {

    }

    /**
     * Возвращает список объектов технологии
     * <p>
     * Для каждого контейнера атрибутов т.е. для каждого набора значений "атрибут наименование/значение"
     * для каждого элемента технологии делается:
     * если у объекта технологии есть атрибут с именем как в наборе значений, то к значению атрибута объекта технологии добавляется значение атрибута из контейнера
     * если у объекта технологии нет атрибута с именем как в наборе значенией, то к элементу технологии добавляется атрибут с соответствующим именем/значением
     *
     * @param basicElements        первоначальный список объектов технологии. там могут быть объекты различных типов
     * @param containersAttributes список контейнеров атрибутов наименование/значение
     * @return
     */
    @Override
    public List<BasicElement> mix(List<BasicElement> basicElements, List<BasicElement> containersAttributes) {

        String SEPARATOR_BETWEEN_ATTR_VALUE = " ";
        List<BasicElement> result = new ArrayList<>();

        //перебираем контейнеры атрибутов
        for (BasicElement containerAttribute : containersAttributes) {
            //перебираем объекты технологии
            for (BasicElement currentBasicElement : basicElements) {
                BasicElement cloneBasicElement = currentBasicElement.clone();

                //перебираем атрибуты в контейнере
                Map<String, String> attrsFromContainer = containerAttribute.getAttributes();
                for (Map.Entry<String, String> nameValueAttribute : attrsFromContainer.entrySet()) {
                    String name = nameValueAttribute.getKey();
                    String value = nameValueAttribute.getValue();
                    //если у элемента есть уже атрибут с указаным названием
                    if (currentBasicElement.getAttributes().containsKey(name)) {
                        String newValueAttr = new StringBuilder(currentBasicElement.getAttributes().get(name))
                                .append(SEPARATOR_BETWEEN_ATTR_VALUE).append(value).toString();
                        //изменим значение атрибута
                        cloneBasicElement.getAttributes().put(name, newValueAttr);
                    } else {
                        //добавим атрибут
                        cloneBasicElement.getAttributes().put(name, value);
                    }
                }
                result.add(cloneBasicElement);
            }
        }
        return result;
    }
}
