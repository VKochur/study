package ru.profit.teamcenter.import_tp.app.model.atrib_tp;

import ru.profit.teamcenter.import_tp.app.model.build_tp.BuilderTp;
import ru.profit.teamcenter.import_tp.app.model.mapping_config.fox.MappingFromFoxToTpConfig;
import ru.profit.teamcenter.import_tp.app.model.repository.TpContent;
import ru.profit.teamcenter.import_tp.app.model.repository.TpContentImpl;
import ru.profit.teamcenter.import_tp.app.model.tp.BasicElement;
import ru.profit.teamcenter.import_tp.app.model.tp.TpConfig;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BuilderTpFromAtrib {

    List<BasicElement> roots = new ArrayList<>();

    public List<BasicElement> getRoots() {
        return roots;
    }

    public void buildTpFromAtrib(AtribTp atribTp, String dirPath, TpConfig tpConfig, MappingFromFoxToTpConfig mapping) throws Exception {
        StringBuilder errMessageBuilder = new StringBuilder();
        List<String> filenames = atribTp.getFilenames();
        Collections.sort(atribTp.getFilenames());
        for (String filename : filenames){
            File file = new File(dirPath + filename);
            if (!file.exists()){
                errMessageBuilder.append(dirPath).append(filename).append("\n");
                continue;
            }else{
                TpContent tpContent = new TpContentImpl();
                BuilderTp builderTp = new BuilderTp();
                BasicElement root = new BasicElement();
                root.setType("TP");
                builderTp.build(root, tpContent.getContent(dirPath + filename), mapping, tpConfig);
                roots.add(root);
            }
        }
        if (errMessageBuilder.length() > 0){
            throw new FilesNotFoundException(errMessageBuilder.append("NOT FOUND").toString());
        }
    }
}
