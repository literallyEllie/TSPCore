package net.tsp.core.mongo;

import com.google.gson.reflect.TypeToken;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import net.tsp.core.Module;
import net.tsp.core.TSPPlugin;
import net.tsp.core.server.settings.ServerConfigKey;
import org.bson.Document;

import java.util.Map;

/**
 * @author Ellie :: 26/07/2019
 */
public class MongoManager extends Module {

    private MongoClient client;
    private MongoDatabase mongoDatabase;

    public static final String DATABASE_PRODUCTION = "tsp_prod", DATABASE_DEVELOPMENT = "tsp_dev";

    public MongoManager(TSPPlugin plugin) {
        super(plugin, "Mongo Manager");
    }

    @Override
    public void start() {
        MongoSettings mongoSettings = new MongoSettings();

        mongoSettings.readNow(new TypeToken<Map<String, String>>(){});

        MongoCredential mongoCredential = MongoCredential.createCredential(
                mongoSettings.getField("username"),
                mongoSettings.getField("database"),
                ((String) mongoSettings.getField("password")).toCharArray());

        this.client = new MongoClient(new ServerAddress((String) mongoSettings.getField("host"),
                Integer.parseInt(mongoSettings.getField("port"))), mongoCredential, MongoClientOptions.builder().build());

        this.mongoDatabase = client.getDatabase(getPlugin().getServerSettings().getSetting(ServerConfigKey.DEV) ? DATABASE_DEVELOPMENT : DATABASE_PRODUCTION);

    }

    @Override
    public void end() {
        client.close();
    }

    // Safety
    public MongoCollection<Document> getCollection(MongoCollectionId mongoCollection) {
        return mongoDatabase.getCollection(mongoCollection.name().toLowerCase());
    }

}
