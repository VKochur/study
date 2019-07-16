package ru.profit.teamcenter.import_tp.app.model.tp;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Элемент модели в дереве ТП
 */
public class BasicElement implements Cloneable{

    private String type;
    private Map<String,String> attributes;
    private BasicElement parent;
    private List<BasicElement> childs;

    public BasicElement() {
        childs = new LinkedList<>();
    }

    public BasicElement addChild(BasicElement child){
        childs.add(child);
        return child;
    }

    public String getType() {
        return type;
    }

    public BasicElement getParent() {
        return parent;
    }

    public List<BasicElement> getChilds() {
        return childs;
    }

    public void setChilds(List<BasicElement> childs) {
        this.childs = childs;
    }

    public void setParent(BasicElement parent) {
        this.parent = parent;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Map<String, String> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, String> attributes) {
        this.attributes = attributes;
    }

    /**
     * Глубокое копирование
     * @return
     */
    @Override
    public BasicElement clone() {
        BasicElement clone = new BasicElement();
        clone.type = this.type;
        //String не изменяемые объекты, поэтому у клона мап будет ссылаться на те же объекты что и оригинал
        clone.attributes = new HashMap<>(this.attributes);
        for (BasicElement child : childs) {
            BasicElement chlidClone = child.clone();
            clone.addChild(chlidClone);
        }
        return clone;
    }

    @Override
    public String toString() {
        StringBuilder aboutChilds = new StringBuilder();
        for (BasicElement child : childs) {
            aboutChilds.append(child.getType()).append(" ").append(child.getAttributes()).append(";");
        }
        return "BasicElement{" +
                "type='" + type + '\'' +
                ", attributes=" + attributes +
                ", childs=" + aboutChilds.toString() +
                '}';
    }

    public static void relocationChildToParent(BasicElement parent, BasicElement child) {
        parent.addChild(child);
        BasicElement oldParent = child.getParent();
        if (oldParent != null) {
            oldParent.getChilds().remove(child);
        }
        child.setParent(parent);
    }

    public static BasicElement createRelationToCloneChild(BasicElement parent, BasicElement child) {
        BasicElement clone = child.clone();
        relocationChildToParent(parent, clone);
        return clone;
    }

    public static BasicElement createRelationToCloneChild(BasicElement parent, BasicElement child, Integer numberForInsert) {
        BasicElement clone = child.clone();
        parent.getChilds().add(numberForInsert, clone);
        clone.setParent(parent);
        return clone;
    }

    /**
     * Получает строковое визуальное представление дерева технологического процесса
     * @param element корень
     * @param maxLevelNesting максимальный уровень вложенности до которого разворачивать
     * @return строку, визуализирующую дерево
     */
    public static String showFullTree(BasicElement element, int maxLevelNesting){
        return showTree(element, maxLevelNesting, 1);
    }

    private static String showTree(BasicElement element, int maxLevel, int currentLevel) {
        StringBuilder result = new StringBuilder(element.type).append(" ").append(element.getAttributes()).append("\n");
        if (maxLevel == currentLevel) {
            return result.toString();
        }

        StringBuilder indention = new StringBuilder();
        for (int i = 0; i < currentLevel; i++) {
            indention.append("   ");
        }
        for (BasicElement child : element.getChilds()) {
            StringBuilder stringBuilder = new StringBuilder(indention).append(showTree(child, maxLevel, currentLevel + 1));
            result.append(stringBuilder).append("\n");
        }
        return result.toString();
    }
}
