package net.tsp.core.redis;

import com.google.gson.reflect.TypeToken;
import net.tsp.core.server.settings.ServerFile;
import net.tsp.core.util.D;
import net.tsp.core.util.MapBuilder;

import java.io.File;
import java.util.Map;

/**
 * @author Ellie :: 26/07/2019
 */
public class RedisSettings extends ServerFile {

    public RedisSettings() {
         super(new File("REDIS"));

         if (!getFile().exists()) {
             D.d("Redis will not run on this sesssion.");
             return;
         }

         readNow(new TypeToken<Map<String, Object>>(){});

        if (getFields() == null) {
            getFile().delete();

            writeDefaultsAndRead(new MapBuilder<String, String>()
                    .linked()
                    .put("host", "localhost")
                    .put("port", "6379")
                    .put("auth", "example")
                    .getMap(), new TypeToken<Map<String, String>>(){});

            System.out.println("##########################");
            System.out.println("### Please fill in REDIS file and restart ###");
            System.out.println("##########################");
        }


    }

}
