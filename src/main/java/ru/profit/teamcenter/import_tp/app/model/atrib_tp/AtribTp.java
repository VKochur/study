package ru.profit.teamcenter.import_tp.app.model.atrib_tp;

import com.linuxense.javadbf.DBFException;
import com.linuxense.javadbf.DBFReader;
import com.linuxense.javadbf.DBFUtils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class AtribTp {
    private List<String> filenames;

    public List<String> getFilenames() {
        return filenames;
    }

    public AtribTp(String key){
        filenames = new ArrayList<>();
        DBFReader reader = null;
        try {
            reader = new DBFReader(new FileInputStream(key), Charset.forName("Cp866"));

            Object[] rowObjects;

            while ((rowObjects = reader.nextRecord()) != null) {

                filenames.add(rowObjects[0].toString() + ".DBF");
            }

        } catch (DBFException | FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            DBFUtils.close(reader);
        }
    }


}
