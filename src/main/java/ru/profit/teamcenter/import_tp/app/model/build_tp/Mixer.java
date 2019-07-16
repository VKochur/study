package ru.profit.teamcenter.import_tp.app.model.build_tp;

import ru.profit.teamcenter.import_tp.app.model.tp.BasicElement;

import java.util.List;

public interface Mixer {

    /**
     * Миксер, который знает как смешивать элементы технологии полученные на основе базовой строки и
     * список контейнеров атрибутов, полученных на основе вспомогательной строки
     * @param basicElements
     * @param containersAttributes
     * @return
     */
     List<BasicElement> mix(List<BasicElement> basicElements, List<BasicElement> containersAttributes);

}
