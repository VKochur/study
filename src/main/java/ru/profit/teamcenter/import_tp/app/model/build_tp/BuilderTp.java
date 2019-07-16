package ru.profit.teamcenter.import_tp.app.model.build_tp;

import ru.profit.teamcenter.import_tp.app.model.mapping_config.fox.MappingFromFoxToTpConfig;
import ru.profit.teamcenter.import_tp.app.model.tp.BasicElement;
import ru.profit.teamcenter.import_tp.app.model.tp.TpConfig;

import java.util.*;

/**
 * Строит модель ТП на основе данных:
 *
 * 1.Корень технологии (объект модели ТП, обозначающий сам ТП)
 *
 * 2.Содержание технологии (список строк, состоящий из подстрок).
 * Информации в каждой строке сооветствуют или не соответствуют новые объекты ТП.
 * Если соответствуют новые объекты (или соответствует новый объект), считаем строку основной.
 * Если не соответствует, считает строку вспомогательный (этот тип строк содержит дополнительную информацию для объектов ТП, которые получаются на
 * основе информации из основных строк).
 *
 * 3.Правила маппинга данных из строк технологии в модель ТП
 * Для правил маппинга есть ограничения:
 *   a. Если указана вспомогательная строка для какой либо базовой строки,
 *      то базовая строка должна переводиться в единственный тип объекта технологии, не несколько.
 *      Например: если строке A соответствует 2 типа - операция и инструкция, не должно быть вспомогательной строки
 *      для которой в качестве основной указана A
 *
 * 4.Конфигурация модели ТП
 * Есть ограничения: не должно быть типов "CONTAINER_INFO_FOR_SUPPORTING_LINE", "UNKNOWN_TYPE"
 *
 * Конфигурация модели ТП и конфигурация маппинга должна быть такой, что если в конфиге мапинга из Фокс одному типу строки соответствует несколько типов объектов технологии,
 * в модели не должно быть неоднозначности какой тип в какой входит (по правилу "состоит из").
 * т.е. типы могут не иметь связи между собой "состоит из", но не должно быть (тип1 состоит из тип 2, и тип2 состоит из тип1), а также не должно быть
 * (тип 3 может входить и в тип1 и в тип2).
 *
 *
 *
 *  Принцип построения:
 *
 * Подчиненность объектов ТП, вложенность друг в друга определяется последовательностью строк, на основе информации в которых объекты строятся.
 * Смотрим на список строк сверху -вниз - строки с меньшим индексом сверху. Объекты для строк сверху являются потенциальными родителями
 * для объектов построенных на основе строк внизу.
 * Также объекты построенные на основании информации из одной строки тоже могут входить друг в друга.
 *
 * Если строка является вспомогательной для нескольких типов строк, то данные относящиеся к вспомогательной строке будут
 * учтены в "ближайшей" основной строке.
 * Если для основной строки в соответствие получается несколько объектов технологии одного типа, то информация по вспомогательной строке добавляется ко
 * всем объектам технологий.
 * Если для одной основной строки соотносится несколько вспомогательных, то информация по всем вспомогательным строкам будет добавлена к объектам
 * относящимся к основной строке.
 * Порядок добавления информации по вспомогательным строкам соответствует близости вспомогательной строки к основной.
 * Информация по более близким строкам добавляется в первую очередь. Если вспомогательные строки находятся на одинаковом удалении от основной (слева и справа),
 * то в начале добавляется информация по вспомогательной строке имеющей меньший индекс.
 *
 * Принцип смешивания информации из основных и вспомогательных строк - к значениям атрибутов полученных из основной строки добавляются значения
 * соответствующих атрибутов, полученных из вспомогательной строки (соответствие атрибутов - по наименованию атрибута).
 * Если по вспомогательной строке есть атрибуты, которых нет по основной строке, то они добавляются к результирующему объекту ТП.
 * Смешивание выполняет Mixer
 */
public class BuilderTp {

    public static final String TYPE_ELEMENT_INFO_SUPPORTING_LINE = "CONTAINER_INFO_FOR_SUPPORTING_LINE";
    public static final String TYPE_FOR_UNKNOWN_TYPE = "UNKNOWN_TYPE";

    //соответствие номеров строк и типов строк
    private Map<Integer, String> lineNumberToSign;

    //соответствие номеров строк и элементов технологического процесса,
    //которые создаются на основе информации в строках (не учитывается инфо в вспомогательных строках)
    private Map<Integer, List<BasicElement>> lineNumberToElements;

    //для вспомогательных строк. соответствие номеров строк и информации об атрибут/значение (список, так как могут быть множественные значения)
    private Map<Integer, List<BasicElement>> lineNumberSupportingLineToAttributes;

    //соответствие на какие номера базовых строк влияют вспомогательные строки
    private Map<Integer, List<Integer>> influenceOnBasicFromSupporting;

    public BuilderTp() {

        //сохраним порядок внесения связей строк с получаемыми элементами
        //данные заполняемые на этапе первичного анализа
        lineNumberToSign = new LinkedHashMap<>();
        lineNumberToElements = new LinkedHashMap<>();
        lineNumberSupportingLineToAttributes = new LinkedHashMap<>();

        //данные заполняемые на этапе вторичного анализа для смешивания информации основных и вспомогательных строк
        influenceOnBasicFromSupporting = new LinkedHashMap<>();
    }

    public BasicElement build(BasicElement rootTp, List<String> content, MappingFromFoxToTpConfig fromFoxMappingConfig, TpConfig tpConfig) throws IllegalFormatConfigurationException {

        checkConfigurations(fromFoxMappingConfig, tpConfig);

        fillingMapsData(content, fromFoxMappingConfig);
        fillingInfluenceSupportingOnBaseLines(fromFoxMappingConfig);
        //смешаем информацию - получим соответствие объектов технологии (lineNumberToElement) с учетом информации в вспомогательных строках
        mixBaseAndSupportingLines();
        //создадим модель технологии, вложенность элементов
        buildModel(rootTp, tpConfig);
        return rootTp;
    }

    private void checkConfigurations(MappingFromFoxToTpConfig fromFoxMappingConfig, TpConfig tpConfig) throws IllegalFormatConfigurationException {
        ConfigurationChecker checker = new ConfigurationChecker();
        if (!checker.checkConfigurations(fromFoxMappingConfig, tpConfig)){
            throw new IllegalFormatConfigurationException(checker.getErrorMessage());
        }
    }

    private void buildModel(BasicElement rootTp, TpConfig tpConfig) {
        List<List<BasicElement>> linkNumberLineToListElements = new ArrayList<>();
        for (Integer number : lineNumberToElements.keySet()) {
            linkNumberLineToListElements.add(lineNumberToElements.get(number));
        }
        new BuilderBranch().buildBranch(rootTp, linkNumberLineToListElements, tpConfig);
    }

    /**
     *
     */
    private void mixBaseAndSupportingLines() {
        //пересматриваем базовые строки на которые существует влияние вспомогательных
        for (Integer basicNumber : influenceOnBasicFromSupporting.keySet()) {
            List<Integer> supportingLinesNumbers = influenceOnBasicFromSupporting.get(basicNumber);
            List<BasicElement> linkedElements = lineNumberToElements.get(basicNumber);
            //перебираем номера вспомогательных строк
            for (Integer supportingLinesNumber : supportingLinesNumbers) {
                //для каждой вспомогательной строки берем связанную информацию и смешиваем с базовыми элементами
                List<BasicElement> infoContaners = lineNumberSupportingLineToAttributes.get(supportingLinesNumber);
                String mixerName = defineNameMixer(lineNumberToSign.get(basicNumber), lineNumberToSign.get(supportingLinesNumber));
                Mixer mixer;
                try {
                    mixer = MixerFactory.getInstance(mixerName);
                } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
                    throw new IllegalStateException("Init mixer. Exception throws: " + e.getMessage());
                }
                //смешивем информацию об элементах технологии полученных на основе базовой строки с информацией
                //полученной с вспомогательной строки
                linkedElements = mixer.mix(linkedElements, infoContaners);
                //изменяем связь номера базовой строки ч объектами технологии
                lineNumberToElements.put(basicNumber, linkedElements);
            }
        }
    }

    private String defineNameMixer(String basicLineSign, String supportingLineSign) {
        return basicLineSign + "_"+supportingLineSign;
    }

    /**
     * Заполним соответствие this.influenceOnBasicFromSupporting на какие номера базовых строк влияют какие вспомогательные
     * При этом список номеров вспомогательных строк, влияющих на базовую отсортирован по расстоянию к базовой строке
     * @param fromFoxMappingConfig
     */
    private void fillingInfluenceSupportingOnBaseLines(MappingFromFoxToTpConfig fromFoxMappingConfig) {
        List<String> placeForSearch = new ArrayList<>(lineNumberToSign.values());
        //пересмотриваем вспомогательные строки
        for (Map.Entry<Integer, List<BasicElement>> numberLineToListContainers : lineNumberSupportingLineToAttributes.entrySet()) {
            Integer numberSupportingString = numberLineToListContainers.getKey();
            String signForSupportingLine = lineNumberToSign.get(numberSupportingString);
            //на какие базовые строки влияет текущая вспомогательная
            List<String> signsOfBasedLines = fromFoxMappingConfig.getSignsOfBasedLines(signForSupportingLine);
            //находим для текущей вспомогательной ближайшую подходящую базовую с учетом направления поиска
            try {
                //номер подходящей базовой
                Integer basicLineNumber = searchBasicLine(numberSupportingString, signsOfBasedLines, fromFoxMappingConfig.goUpForDefineBaseString(signForSupportingLine), placeForSearch);
                //дополним информацию для базовой строки, что на нее влияет текущая вспомогательная
                if (influenceOnBasicFromSupporting.get(basicLineNumber) != null) {
                    influenceOnBasicFromSupporting.get(basicLineNumber).add(numberSupportingString);
                } else {
                    List<Integer> supportingStringsNumbers = new ArrayList<>();
                    supportingStringsNumbers.add(numberSupportingString);
                    influenceOnBasicFromSupporting.put(basicLineNumber, supportingStringsNumbers);
                }
            }  catch (NoSuchElementException e) {
                //данные такие, что не найдено дя текущей вспомагательной соответствующей базовой
            }

        }
        //здесь в influenceOnBasicFromSupporting должна быть инфо как на номера базовых строк влияют вспомогательные
        //отсортируем по расстоянию между вспомогательными строками и основной
        for (Integer baseLineNumber : influenceOnBasicFromSupporting.keySet()) {
            List<Integer> supportingNumbers = influenceOnBasicFromSupporting.get(baseLineNumber);
            Collections.sort(supportingNumbers, (o1, o2) -> Math.abs(o1 - baseLineNumber) - Math.abs(o2 - baseLineNumber));
        }
    }

    /**
     * Поиск номера базовый строки.
     * Поиск ведется в lineNumberToSign
     *
     * @param from              с какой строки начинается поиск
     * @param signsOfBasedLines список подходящих типов строк
     * @param goUp              true - направление поиска вверх (к началу списка), false - вниз
     * @param where             место для поиска
     * @return номер стоки с подходящим базовым элементом
     * @throws NoSuchElementException если не найдено подходящей строки
     */
    private Integer searchBasicLine(Integer from, List<String> signsOfBasedLines, Boolean goUp, List<String> where) throws NoSuchElementException {
        int increment = (goUp) ? -1 : 1;

        for (int number = from + increment; (number > -1) && (number < where.size()); number += increment) {
            if (signsOfBasedLines.contains(where.get(number))) {
                return number;
            }
        }
        throw new NoSuchElementException("For " + where + "from " + from + "not found suitable base line. Suitable are " + signsOfBasedLines);
    }

    /**
     * Первичное заполнение массивов данных для дальнейшего анализа
     * <p>
     * //соответствие номеров строк и типов строк
     * private Map<Integer, String> lineNumberToSign;
     * <p>
     * //соответствие номеров строк и элементов технологического процесса,
     * //которые создаются на основе информации в строках (не учитывается инфо в вспомогательных строках)
     * private Map<Integer, List<BasicElement>> lineNumberToElements;
     * <p>
     * //для вспомогательных строк. соответствие номеров строк и информации об атрибут/значение (список, так как могут быть множественные значения)
     * private Map<Integer, List<BasicElement>> lineNumberSupportingLineToAttributes;
     *
     * @param content
     * @param fromFoxMappingConfig
     */
    private void fillingMapsData(List<String> content, MappingFromFoxToTpConfig fromFoxMappingConfig) {
        for (int i = 0; i < content.size(); i++) {
            lineNumberToSign.put(i, defineSignIgnoreCheckValidArg(content.get(i), fromFoxMappingConfig));
            List<BasicElement> associationsElements;
            try {
                associationsElements = defineAssociationsElements(content.get(i), fromFoxMappingConfig);
                lineNumberToElements.put(i, associationsElements);
            } catch (NoSuchElementException e) {
                //если это вспомогательная строка
                associationsElements = defineAttributeBySupportingLine(content.get(i), fromFoxMappingConfig);
                lineNumberSupportingLineToAttributes.put(i, associationsElements);
            }
        }
    }


    /**
     *
     *
     * @param line
     * @param fromFoxMappingConfig
     * @return Базовые элементы технологии. Строются по строкам вспомогательным. Являются просто контейнерами для значений атрибутов.
     */
    private List<BasicElement> defineAttributeBySupportingLine(String line, MappingFromFoxToTpConfig fromFoxMappingConfig) {
        String lineSign = defineSignIgnoreCheckValidArg(line, fromFoxMappingConfig);
        List<String> types = new ArrayList<>();
        types.add(TYPE_ELEMENT_INFO_SUPPORTING_LINE);
        List<Map<Integer, String>> numberSubstringAndAttribute = fromFoxMappingConfig.getNumberSubstringAndAttribute(lineSign);
        Map<Integer, String> separatorsForSubstrings = fromFoxMappingConfig.getSeparatorsForSubstringsInLines().get(lineSign);
        return createElements(types, numberSubstringAndAttribute, separatorsForSubstrings, line.split("\\\\"));
    }

    private String defineSignIgnoreCheckValidArg(String line, MappingFromFoxToTpConfig mappingFromFoxToTpConfig) {
        String[] substrings = line.split("\\\\");
        return substrings[mappingFromFoxToTpConfig.getNumberSubStringForSign()];
    }

    /**
     * Получить связянные со строкой элементы технологии
     * <p>
     * Если в мапинге указано что строке соответствует несколько типов,
     * то в результирующем списке порядок объектов совпадает с mappingFromFoxToTpConfig.defineType()
     * <p>
     * Если есть правила указывающие на наличие атрибутов с множественным значением, то в результирующем списке элементов содержится несколько элементов
     * нужного типа (в мапинге также есть инфо какая подстрока это атрибут для какого типа объекта в технологии),
     * которые соответствуют всем возможным комбинациям значений множественных атрибутов.
     * т.е. в строке указаны множественные 2 подстроки (т.е. 2 атрибута), такие что:
     * атрибут1/значение1,значение2,значение3
     * атрибут2/значение12,значение22
     * и а мапинге этой строке соответствует тип объекта - ТИП,
     * то ТИПу будут соответствовать 6 объектов "атрибут1 = значение1, атрибут2 = знаачение12", "атрибут1 = значение1, атрибут2 = значение22" и т.д.
     *
     * @param line                 строка
     * @param fromFoxMappingConfig правила маппинга строк в элементы технологии
     * @return список элементов технологий, соответствующих строке. В одной строке может быть указано несколько типов объектов (например операция и инструкция),
     * а также несколько объектов одного типа (когда значения атрибута является множественным и в маппинге указаны правила разделения значений)
     * @throws NoSuchElementException если рассматриваемая строка является вспомогательной для других строк. т.е. по не не строятся никакие элементы технологии
     */
    private List<BasicElement> defineAssociationsElements(String line, MappingFromFoxToTpConfig fromFoxMappingConfig) throws NoSuchElementException {
        String[] subStrings = line.split("\\\\");
        String lineSign = subStrings[fromFoxMappingConfig.getNumberSubStringForSign()];
        try {
            //если строка является основной, т.е. по ней строятся объекты в технологии
            List<String> types = fromFoxMappingConfig.defineType(lineSign); //throws NSEE
            List<Map<Integer, String>> numberSubstringAndAttribute = fromFoxMappingConfig.getNumberSubstringAndAttribute(lineSign);
            Map<Integer, String> separatorsForSubstrings = fromFoxMappingConfig.getSeparatorsForSubstringsInLines().get(lineSign);
            return createElements(types, numberSubstringAndAttribute, separatorsForSubstrings, subStrings);
        } catch (NoSuchElementException e) {
            //строка является вспомогательной, т.е. есть где-то основная строка по которой построится объект используя инфо и текущей строки
            throw new NoSuchElementException("Not able define tp's element by supporting line: " + line);
        } catch (IllegalArgumentException e) {
            //попалась строка в файле неизвестного типа
            return processingWrongLine(line);
        }
    }

    /**
     * какие элемемнты возвращаем, если строка не известного формата
     *
     * @param line
     * @return Элемент типа UNKNOWN_TYPE
     */
    private List<BasicElement> processingWrongLine(String line) {
        BasicElement element = new BasicElement();
        element.setType(TYPE_FOR_UNKNOWN_TYPE);
        Map<String, String> attr = new HashMap<>();
        element.setAttributes(attr);
        attr.put("originalLine", line);
        return Arrays.asList(element);
    }


    private List<BasicElement> createElements(List<String> types, List<Map<Integer, String>> numberSubstringAndAttribute, Map<Integer, String> separatorsForSubstrings, String[] subStrings) {
        if (separatorsForSubstrings == null) {
            //---------------------создаем объекты в условиях когда нет разделителей значений атрибутов-----------------

            //для атрибутов не указано никаких разделителей значений
            //значит для одного типа соответствует один мап с информацией о названии/значении атрибутов
            //поэтому - тип создаваемого объекта и его атрибуты имя/значение
            Map<String, Map<String, String>> typeAndAttribNameValue = new LinkedHashMap<>();

            //разбираем последовательно типы
            for (int i = 0; i < types.size(); i++) {
                Map<String, String> attributes = new TreeMap<>();
                Map<Integer, String> mappingAttr = numberSubstringAndAttribute.get(i);
                for (Integer substringNumber : mappingAttr.keySet()) {
                    attributes.put(mappingAttr.get(substringNumber), subStrings[substringNumber]);
                }
                typeAndAttribNameValue.put(types.get(i), attributes);
            }

            //нужные типы с набором атрибутов
            List<BasicElement> elements = new ArrayList<>();
            for (String type : typeAndAttribNameValue.keySet()) {
                BasicElement element = new BasicElement();
                element.setType(type);
                element.setAttributes(typeAndAttribNameValue.get(type));
                elements.add(element);
            }
            return elements;

        } else {

            //-------------------создаем объекты в условиях когда присутствуют разделителей значений атрибутов-------------------

            //в одном атрибуте содержится несколько значений
            //у нас не один набор значений атрибутов, а несколько
            //значит для одного типа может быть несколько мап с информацией о названии/значении атрибутов
            Map<String, List<Map<String, String>>> typeAndListMapsAboutAttr = new LinkedHashMap<>();

            //разбираем последовательно типы
            for (int i = 0; i < types.size(); i++) {
                //какие подстроки для конкретного типа соответствуют атрибутам
                Map<Integer, String> mappingAttr = numberSubstringAndAttribute.get(i);

                //разбираем для данного типа есть ли атрибуты, значения для которых пишутся в подстроке через разделители
                //множество номеров подстрок сооветсвующих атрибутам текущего типа
                Set<Integer> numbersOfSubstringsOfType = mappingAttr.keySet();
                //множество номер подстрок, значения которых могут быть разделены сепаратором
                Set<Integer> numberOfSubstringsContainsSeparator = separatorsForSubstrings.keySet();
                //номера подстрок, относящиеся к рассматриваемому типу и имеющие разделитель для значений
                Set<Integer> intersection = new HashSet<>(numbersOfSubstringsOfType);
                intersection.retainAll(numberOfSubstringsContainsSeparator);
                //какие значения в итоге принимают соответствующие номера подстрок
                Map<Integer, List<String>> valuesForMultiSubstrings = new HashMap<>();
                for (Integer numberSubstring : intersection) {
                    String separator = separatorsForSubstrings.get(numberSubstring);
                    List<String> valuesAttrBySpecificSubstring = new ArrayList(Arrays.asList(subStrings[numberSubstring].split(separator)));
                    valuesForMultiSubstrings.put(numberSubstring, valuesAttrBySpecificSubstring);
                }
                //в итоге есть: подстроки, номера подстрок которые относятся к текущему типу и названия атрибутов соответств. подстрокам
                //также есть номера подстрок значения которых являются множественными и сами эти значения
                //получим список наборов атрибутов название/значение для данного типа
                List<Map<String, String>> listMapAttr = solveMapAttr(subStrings, mappingAttr, valuesForMultiSubstrings);
                //заполним информацию - тип, и список наборов атрибутов
                typeAndListMapsAboutAttr.put(types.get(i), listMapAttr);

            }

            //нужные типы с набором атрибутов
            List<BasicElement> elements = new ArrayList<>();
            for (String type : typeAndListMapsAboutAttr.keySet()) {
                List<Map<String, String>> groupsAttr = typeAndListMapsAboutAttr.get(type);
                for (Map<String, String> attributes : groupsAttr) {
                    BasicElement element = new BasicElement();
                    element.setType(type);
                    element.setAttributes(attributes);
                    elements.add(element);
                }
            }
            return elements;
        }
    }

    /**
     * На входе подстроки (в которых указаны значения атрибутов, возможно и множественным образом),
     * номера подстрок и значения названий атрибутов,
     * номера подстрок(каждая соответствует некоторому названию аритубута и множественные значения атрибута)
     * <p>
     * На выходе спиоок наборов информации о наличии атрибутов в виде наименование/значение
     *
     * @param subStrings
     * @param mappingAttr
     * @param valuesForMultiSubstrings
     * @return
     */
    private List<Map<String, String>> solveMapAttr(String[] subStrings, Map<Integer, String> mappingAttr, Map<Integer, List<String>> valuesForMultiSubstrings) {

        //в итоге у нас долны быть наборы атрибутов(считаем есть шкаф наборов атрибутов, где набор атрибутов лежит на полке),
        // среди них есть такие какие меняются от полки к полке, а есть такие, которые и не меняются

        //атрибуты, значения которых одинаковы
        Map<String, String> attribsGeneralForAll = new TreeMap<>();

        //у нас есть множества номеров подстрок всех
        Set<Integer> attrAll = new HashSet<>(mappingAttr.keySet());
        //множество подстрок имеющих разделитель для значений
        Set<Integer> attrThatHaveSeparator = valuesForMultiSubstrings.keySet();
        //оставим номера подстрок, где значения атрибутов не разделяется сепаратором
        attrAll.removeAll(attrThatHaveSeparator);
        for (Integer numberOfSubstring : attrAll) {
            attribsGeneralForAll.put(mappingAttr.get(numberOfSubstring), subStrings[numberOfSubstring]);
        }

        //в i-том элементе, значение номера подстроки (потом используем для определения по номеру подстроки названия атрибута)
        List<Integer> assosiationColumnAndNumberSubstring = new ArrayList<>();
        //в i-том столбце значения которые может принимать атрибут соответствующий подстроке с номером assosiationColumnAndNumberSubstring[i]
        List<List<String>> matrixInColumnAreDifferentValuesForAttrib = new ArrayList<>();
        for (Map.Entry<Integer, List<String>> integerListEntry : valuesForMultiSubstrings.entrySet()) {
            assosiationColumnAndNumberSubstring.add(integerListEntry.getKey());
            matrixInColumnAreDifferentValuesForAttrib.add(integerListEntry.getValue());
        }

        //теперь нам надо получить все возможные комбинации значений атрибутов
        List<List<String>> allCombinationsOfValuesAttr = new ArrayList<>();
        for (List<String> columnStrings : matrixInColumnAreDifferentValuesForAttrib) {
            allCombinationsOfValuesAttr = addColumn(allCombinationsOfValuesAttr, columnStrings);
        }

        //здесь у нас должны быть все возможные комбинации значений атрибутов в allCombinationsOfValuesAttr
        //строка - это набор значений атрибутов, номер столбца это под этим номером в assosiationColumnAndNumberSubstring хранится
        //номер подстроки которой соответствует значение атрибута. Само название атрибута в mappingAttr, где ключ - номер подстроки

        //атрибуты значения которых меняются от полки к полке
        List<Map<String, String>> attribsNotGeneralForAll = new ArrayList<>();

        //здесь смотреть
        //пересматриваем все комбинации значений атрибутов
        for (int j = 0; j < allCombinationsOfValuesAttr.size(); j++) {
            List<String> attributes = allCombinationsOfValuesAttr.get(j);
            Map<String, String> attrsNameValue = new TreeMap<>();
            //пересматриваем значения атрибутов
            for (int i = 0; i < attributes.size(); i++) {
                Integer substringNumber = assosiationColumnAndNumberSubstring.get(i);
                String attrName = mappingAttr.get(substringNumber);
                String attrValue = attributes.get(i);
                attrsNameValue.put(attrName, attrValue);
            }
            attribsNotGeneralForAll.add(attrsNameValue);
        }
        //до сюда

        //здесь есть наборы атрибутов одинаковых на всех полках, и атрибутов не одинаковых на полках
        //добавим к неодинаковым одинаковые, получим искомый набор полок с полным набором атрибутов на каждом
        List<Map<String, String>> allAttributes = new ArrayList<>();

        if (attribsNotGeneralForAll.isEmpty()) {
            //если нет атрибутов, значения которых разделяются
            allAttributes.add(attribsGeneralForAll);
        } else {
            //если есть атрибуты, значения которых разделяются
            for (Map<String, String> complectAttr : attribsNotGeneralForAll) {
                Map<String, String> attr = new TreeMap<>();
                attr.putAll(attribsGeneralForAll);
                attr.putAll(complectAttr);
                allAttributes.add(attr);
            }
        }

        return allAttributes;
    }

    private List<List<String>> addColumn(List<List<String>> matrix, List<String> columnStrings) {
        List<List<String>> result = new ArrayList<>();
        if (matrix.size() == 0) {
            for (String columnString : columnStrings) {
                List<String> temp = new ArrayList<>();
                temp.add(columnString);
                result.add(temp);
            }
        } else {
            for (String valueFromColumn : columnStrings) {
                List<List<String>> partOfResult = new ArrayList<>(matrix);
                for (List<String> strings : partOfResult) {
                    List<String> line = new ArrayList<>(strings);
                    line.add(valueFromColumn);
                    result.add(line);
                }
            }
        }
        return result;
    }

}
