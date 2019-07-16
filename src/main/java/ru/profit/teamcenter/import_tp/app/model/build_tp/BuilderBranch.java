package ru.profit.teamcenter.import_tp.app.model.build_tp;

import ru.profit.teamcenter.import_tp.app.model.tp.BasicElement;
import ru.profit.teamcenter.import_tp.app.model.tp.TpConfig;

import java.util.*;

/**
 * Строит ветку технологии, а если в качестве корня и контекста подать ТП, то строит ТП
 */
public class BuilderBranch {

    //пакетный доступ
    BuilderBranch() {
    }

    /**
     * Построить ветку технологии
     * <p>
     * Создает вложенность объектов технологии друг в друга (подчиненность "состоит из") в соответствии со списком списка объектов и модели технологии
     * Терминология - список списков объектов смотрим сверху вниз, т.е. от более раннего индекса к более позднему.
     * Объекты находящиеся в более верхних списках считаются потенциальными родителями для объектов находящихся в более нижних списках.
     * <p>
     * Приоритетность вложенности:
     * <p>
     * 1.  Объекты находящиеся на одном уровне списка вкладываются друг в друга в соответствии с моделью технологии
     * Объекты находящиеся на одном уровне и модель технологии должны быть такие, чтобы не было неоднозначности в какой тип объекты взятого в строке
     * входит (но может и не входить) конкретный тип объекты взятый в строке.
     * <p>
     * 2.Для конкретного объекта родителем считается наиболее "близко" вверху расположенный объект допустимого моделью технологии типа.
     * Под расстоянием подразумевается количество строк между дочерним и потенциально родительским объектами.
     * <p>
     * Если рассматриваемая строка содержит несколько объектов разных типов, и для данных типов определены родители различных типов
     * расположенные в одной той же строке находящейся вверху относительно рассматриваемой,
     * то для каждого объекта родителя каждого типа будет создана связь "состоит из" с каждым объектом каждого дочернего типа в рассматриваемой строке.
     *
     * @param root     корневой объект
     * @param content  список списков объектов технологий
     * @param tpConfig конфигурация технологии с информацией о вложенности типов объектов технологии друг в друга
     * @return корневой объект с уже подчиненными к нему другими объектами технологии
     */
    public BasicElement buildBranch(BasicElement root, List<List<BasicElement>> content, TpConfig tpConfig) {

        //добавим на самый верх корень
        content.add(0, new ArrayList<>());
        content.get(0).add(root);

        //информация для каждого объекта в каждой строке сколько у элемента в подчинении у объектов,
        //оставшихся после вложений объектов находящихся на уровне одной строки
        //потом эта инфо используется при подключении новых объектов в подчинении, взятых с других строк.
        //т.к. объекты должны вставляться после уже существующих после вложений, причем в порядке соответствующем строкам
        Map<BasicElement, Integer> countChilds = new HashMap<>();   
        
        //пройдемся по всему списку списков объектов и внутри каждого списка выполним вложение объектов друг в друга,
        //если это позволяет конфигурация
        for (int lineNumber = 0; lineNumber < content.size(); lineNumber++) {
            List<BasicElement> linkedElements = content.get(lineNumber);
            List<BasicElement> nestedElements = tieElementsInLinesTogether(linkedElements, tpConfig);
            content.set(lineNumber, nestedElements);

            for (BasicElement nestedElement : nestedElements) {
                countChilds.put(nestedElement, nestedElement.getChilds().size());
            }
        }

        //ходим снизу списка вверх и вкладываем элементы друг в друга
        for (int i = content.size() - 1; i > 0; i--) {
            List<BasicElement> potentialChilds = content.get(i);
            //бегаем по элементам текущей строки
            for (BasicElement potentialChild : potentialChilds) {
                String typeCurrentElement = potentialChild.getType();
                try {
                    Set<String> allowableTypesParents = tpConfig.allowableTypesParents(typeCurrentElement); //IAE
                    if (allowableTypesParents == null) {
                        //если текущий элемент имеет тип который никуда не может входить, то переходим к другому элементу
                        continue;
                    }
                        //ищем для текущего элемента родителя вверх по списку
                        boolean searchParentsToUp = true;
                        for (int j = i - 1; (j > -1) && searchParentsToUp; j--) {
                            List<BasicElement> potentialParents = content.get(j);
                            //ходим по родителям потенциальным
                            for (BasicElement potentialParent : potentialParents) {
                                String typePotentialParent = potentialParent.getType();
                                if (allowableTypesParents.contains(typePotentialParent)) {
                                    //если на текущей строке нашли родителей, то выше данной строки не смотрим
                                    searchParentsToUp = false;


                                    //а на текущей строке для каждого элемента родителя ставим связь с дочерним объектом
                                    //с клоном, так как один объект может быть привязан к нескольким родителям,
                                    //каждое новое привязывание отсоединяет объект от прежнего родителя
                                    //причем для родителя потомок должен встать впереди других потомков которые подвязались с анализа более дальних строк,
                                    //но после потомков оставшихся от вложений объектов на одной строке
                                    BasicElement.createRelationToCloneChild(potentialParent, potentialChild, countChilds.get(potentialParent));
                                }
                            }
                        }
                } catch (IllegalArgumentException e) {
                    //Элемент не известного типа для конфигурации модели, ничего не делаем
                }
            }
        }
        return root;
    }

    /**
     * Создает связи "состоит из" между объектами, при наличии соответствующих связей в конфигурации модели
     *
     * @param linkedElements список элементов. элементы могут быть различного типа.
     *                       в элементы одного типа данного списка могут входить элементы другого типа данного списка.
     *                       Если в списке несколько элементов родительского типа и несколько дочернего типа (родительский/дочерний -
     *                       в рамках отношения "состоит из"), то для каждого элемента родительского типа создается связь со всеми элементами дочернего типа.
     * @param tpConfig       конфигурация технологии, содержащая информацию о возможных связях "состоит из" между типами объектов ТП
     * @return
     */
    private List<BasicElement> tieElementsInLinesTogether(List<BasicElement> linkedElements, TpConfig tpConfig) {
        boolean[] hidden = new boolean[linkedElements.size()];
        Arrays.fill(hidden, false);
        for (int i = 0; i < linkedElements.size(); i++) {
            BasicElement linkedElement = linkedElements.get(i);
            try {
                //во что элемент может входить потенициальный потомок?
                Set<String> allowableParents = tpConfig.allowableTypesParents(linkedElement.getType()); //IAE
                //если ни во что не может входить, то переходим к следующему элементу
                if (allowableParents == null) {
                    continue;
                }
                //рассматриваем снова все элементы на одной строке в поисках родителя
                for (int j = 0; j < linkedElements.size(); j++) {
                    //если рассматриваемый потомок может входить в текущий родитель
                    if (allowableParents.contains(linkedElements.get(j).getType())) {
                        //создаем связь между родителем и копией потомка
                        BasicElement.createRelationToCloneChild(linkedElements.get(j), linkedElement);
                        //мы объект поместили в подчинении другому объекту, не нужно его выдавать в итоговом списке
                        hidden[i] = true;
                    }
                }
            } catch (IllegalArgumentException e) {
                //элемент не известного типа для конфигурации модели ТП, ничего не делаем
            }
        }
        //список объектов с уже вложенными внутрь объектами, причем вложенные объекты в списке не присутствуют
        List<BasicElement> result = new ArrayList<>();
        for (int i = 0; i < hidden.length; i++) {
            if (!hidden[i]) {
                result.add(linkedElements.get(i));
            }
        }
        return result;
    }
}
