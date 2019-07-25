package net.tsp.core.server.settings;

import com.google.common.collect.Maps;

import java.io.File;
import java.util.Map;

/**
 * @author Ellie :: 25/07/2019
 */
public class ServerSettings extends ServerFile {

    public ServerSettings() {
        super(new File("SERVER_SETTINGS"));

        Map<String, Object> defaults = Maps.newHashMap();

        for (ServerConfigKey value : ServerConfigKey.values()) {
            defaults.put(value.getKey(), value.getDefaultValue());
        }

        writeDefaults(defaults);
    }

    public <T> T getSetting(ServerConfigKey configKey) {
        return (T) getFields().getOrDefault(configKey.getKey(), configKey.getDefaultValue());
    }

}
