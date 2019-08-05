package net.tsp.core.server.settings;

import com.google.common.collect.Maps;
import com.google.gson.reflect.TypeToken;
import net.tsp.core.util.D;

import java.io.File;
import java.util.Map;

/**
 * @author Ellie :: 25/07/2019
 */
public class ServerSettings extends ServerFile {

    public ServerSettings() {
        super(new File("SERVER_SETTINGS"));

        Map<String, Object> defaults = Maps.newLinkedHashMap();

        for (ServerConfigKey value : ServerConfigKey.values()) {
            defaults.put(value.getKey(), value.getDefaultValue());
        }

        writeDefaultsAndRead(defaults, new TypeToken<Map<String, Object>>(){});
    }

    public <T> T getSetting(ServerConfigKey configKey) {
        return (T) getFields().getOrDefault(configKey.getKey(), configKey.getDefaultValue());
    }

}
