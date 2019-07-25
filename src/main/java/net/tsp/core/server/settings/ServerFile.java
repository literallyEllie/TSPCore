package net.tsp.core.server.settings;

import com.google.common.collect.Maps;
import net.tsp.core.TSPPlugin;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

/**
 * @author Ellie :: 25/07/2019
 */
public class ServerFile {

    private File file;
    private Map<String, Object> fields;
    private boolean firstTime;

    public ServerFile(File file) {
        this.file = file;
        this.fields = Maps.newHashMap();
    }

    public void writeDefaults(Map<?, ?> values)
            throws NullPointerException {

        if (file == null)
            throw new NullPointerException("file cannot be null");

        if (!file.exists()) {
            try {
                firstTime = true;
                file.createNewFile();

                write(values);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return;
        }

    }

    private void write(Object object) {

        try {
            TSPPlugin.gson.toJson(object, new FileWriter(file));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public File getFile() {
        return file;
    }

    public boolean doesFileExist() {
        return file != null && file.exists();
    }

    public Map<String, Object> getFields() {
        return fields;
    }

    public <T> T getField(String key)
            throws ClassCastException{
        return (T) fields.get(key);
    }

    public boolean isFirstTime() {
        return firstTime;
    }

}
