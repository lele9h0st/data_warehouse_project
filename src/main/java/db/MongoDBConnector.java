package db;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.ServerApi;
import com.mongodb.ServerApiVersion;
import com.mongodb.client.MongoDatabase;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class MongoDBConnector {
    public static MongoDatabase makeConnect() {
        String username = null;
        String password = null;
        try {
            username = URLEncoder.encode("lele9h0st", "UTF-8");
            password = URLEncoder.encode("hoang2001", "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        ConnectionString connectionString = new ConnectionString("mongodb+srv://" + username + ":" + password + "@cluster0.zdkqg4w.mongodb.net/?retryWrites=true&w=majority");
        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .serverApi(ServerApi.builder()
                        .version(ServerApiVersion.V1)
                        .build())
                .build();
        com.mongodb.client.MongoClient mongoClient = com.mongodb.client.MongoClients.create(settings);
        MongoDatabase database = mongoClient.getDatabase("data_warehouse");
        return database;
    }
}
