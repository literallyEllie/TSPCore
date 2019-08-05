package net.tsp.core.mongo;

import net.tsp.core.server.settings.ServerFile;
import net.tsp.core.util.MapBuilder;

import java.io.File;

/**
 * @author Ellie :: 26/07/2019
 */
public class MongoSettings extends ServerFile {

    public MongoSettings() {
         super(new File("MONGO"), new MapBuilder<String, Object>()
                 .linked()
                 .put("host", "localhost")
                 .put("port", 27017)
                 .put("username", "example")
                 .put("password", "example")
                 .put("database", "example")
                 .getMap());
    }

}
