package net.tsp.core.server.settings;

import com.google.common.collect.Maps;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import net.tsp.core.TSPPlugin;

import java.io.*;
import java.util.Map;

/**
 * @author Ellie :: 25/07/2019
 */
public class ServerFile {

    private File file;
    private Map<?, ?> fields;
    private boolean firstTime;

    public ServerFile(File file) {
        this.file = file;
        this.fields = Maps.newHashMap();
    }

    public ServerFile(File file, Map<?, ?> defaults) {
        this(file);
        writeDefaults(defaults);
    }

    public void readNow(TypeToken typeToken) {
        read(typeToken);
    }

    public void writeDefaultsAndRead(Map<?, ?> defaults, TypeToken typeToken) {
        writeDefaults(defaults);
        read(typeToken);
    }

    public void writeDefaults(Map<?, ?> defaults)
            throws NullPointerException {

        if (file == null)
            throw new NullPointerException("file cannot be null");

        if (!file.exists()) {
            try {
                firstTime = true;
                file.createNewFile();

                write(defaults);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return;
        }

    }

    private void write(Object object) {

        try (Writer writer = new FileWriter(file)) {
            TSPPlugin.gson.toJson(object, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void read(TypeToken typeToken) {

        try (Reader reader = new FileReader(file)) {
            JsonReader jsonReader = new JsonReader(reader);

            this.fields = TSPPlugin.gson.fromJson(jsonReader, typeToken.getType());

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

    public Map<?, ?> getFields() {
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
