package ru.profit.teamcenter.import_tp.app.model.atrib_tp;

import org.junit.Test;
import ru.profit.teamcenter.import_tp.app.model.mapping_config.fox.MappingConfigImpl;
import ru.profit.teamcenter.import_tp.app.model.mapping_config.fox.MappingFormFoxConfigFactory;
import ru.profit.teamcenter.import_tp.app.model.tp.BasicElement;
import ru.profit.teamcenter.import_tp.app.model.tp.TpConfig;
import ru.profit.teamcenter.import_tp.app.model.tp.TpConfigFactory;

import java.nio.file.Paths;

public class BuilderTpFromAtribTest {

    @Test
    public void testBuildTpFromAtrib() throws Exception {
        AtribTp atribTp = new AtribTp("C:\\Users\\User\\Desktop\\ATRIB_TP.DBF");
        String pathConfigTp = Paths.get(getClass().getClassLoader().getResource("configTp.json").toURI()).toString();
        String pathMapping = Paths.get(getClass().getClassLoader().getResource("configMappingFox.json").toURI()).toString();

        TpConfig tpConfig = TpConfigFactory.getInstanceFromJson(pathConfigTp);
        MappingConfigImpl mapping = (MappingConfigImpl) MappingFormFoxConfigFactory.getInstanceFromJson(pathMapping);

        BuilderTpFromAtrib builder = new BuilderTpFromAtrib();
        try{
            builder.buildTpFromAtrib(atribTp,"C:\\Users\\User\\Desktop\\Файлы FOXPro\\",tpConfig,mapping);
        }catch (FilesNotFoundException e){
            e.printStackTrace();
        }

        for (BasicElement root : builder.getRoots()){
            System.out.println(BasicElement.showFullTree(root, 6));
        }
    }
}