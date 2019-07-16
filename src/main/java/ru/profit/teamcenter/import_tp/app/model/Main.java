package ru.profit.teamcenter.import_tp.app.model;

import ru.profit.teamcenter.import_tp.app.model.build_tp.BuilderTp;
import ru.profit.teamcenter.import_tp.app.model.mapping_config.fox.MappingConfigImpl;
import ru.profit.teamcenter.import_tp.app.model.mapping_config.fox.MappingFormFoxConfigFactory;
import ru.profit.teamcenter.import_tp.app.model.repository.TpContent;
import ru.profit.teamcenter.import_tp.app.model.repository.TpContentImpl;
import ru.profit.teamcenter.import_tp.app.model.tp.BasicElement;
import ru.profit.teamcenter.import_tp.app.model.tp.TpConfig;
import ru.profit.teamcenter.import_tp.app.model.tp.TpConfigFactory;

public class Main {
    public static void main(String[] args) throws Exception {
        //корень ТП
        BasicElement root = new BasicElement();
        root.setType("TP");
        TpContent tpContent = new TpContentImpl();
        BuilderTp builderTp = new BuilderTp();
        MappingConfigImpl mapping = (MappingConfigImpl) MappingFormFoxConfigFactory.getInstance("D:\\WS2\\ru.profit.teamcenter.import_tp\\configMappingFox.properties");
        TpConfig tpConfig = TpConfigFactory.getInstance("D:\\WS2\\ru.profit.teamcenter.import_tp\\configTp.properties");

        builderTp.build(root, tpContent.getContent("null"), mapping, tpConfig);
        System.out.println(BasicElement.showFullTree(root, 6));
    }
}
