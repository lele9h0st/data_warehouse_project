import com.mongodb.*;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoIterable;
import com.mongodb.client.result.InsertOneResult;
import org.bson.Document;
import org.bson.types.ObjectId;


import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.util.Collection;
import java.util.List;

public class TestMongDB {
    public static void main(String[] args) throws UnknownHostException, UnsupportedEncodingException {
        String username = URLEncoder.encode("lele9h0st", "UTF-8");
        String password = URLEncoder.encode("hoang2001", "UTF-8");
        ConnectionString connectionString = new ConnectionString("mongodb+srv://" + username + ":" + password + "@cluster0.zdkqg4w.mongodb.net/?retryWrites=true&w=majority");
        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .serverApi(ServerApi.builder()
                        .version(ServerApiVersion.V1)
                        .build())
                .build();
        com.mongodb.client.MongoClient mongoClient = com.mongodb.client.MongoClients.create(settings);
        MongoDatabase database = mongoClient.getDatabase("data_warehouse");
        MongoIterable<String> listConnection = database.listCollectionNames();
        MongoCollection collection = database.getCollection("file_log");
        InsertOneResult result =collection.insertOne(new Document().append("_id",new ObjectId()).append("id_config",1).append("date","1/1/2022").append("status","ER").append("author","hoang"));
        System.out.println("Success! Inserted document id: " + result.getInsertedId());
        System.out.println(collection.countDocuments());


    }

}
