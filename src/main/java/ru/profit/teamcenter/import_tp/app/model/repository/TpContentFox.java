package ru.profit.teamcenter.import_tp.app.model.repository;

import com.linuxense.javadbf.DBFException;
import com.linuxense.javadbf.DBFReader;
import com.linuxense.javadbf.DBFUtils;

import java.io.FileInputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class TpContentFox implements TpContent {
    /**
     *
     * @param key путь к файлу
     * @return
     * @throws Exception
     */

    @Override
    public List<String> getContent(String key) throws Exception {
        List<String> content = new ArrayList<>();

        DBFReader reader = null;
        try {
            reader = new DBFReader(new FileInputStream(key), Charset.forName("Cp866"));

            Object[] rowObjects;

            while ((rowObjects = reader.nextRecord()) != null) {
                content.add(rowObjects[0].toString());
            }

        } catch (DBFException e) {
            e.printStackTrace();
        } finally {
            DBFUtils.close(reader);
        }

        return content;
    }
}
