package crawling;

import com.mongodb.MongoException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.UpdateResult;
import db.MongoDBConnector;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;

public class SendFile {
    public static void main(String[] args) {
        String server = "localhost";
        int port = 21;
        String user = "hoang";
        String pass = "";
        MongoDatabase mdb = MongoDBConnector.makeConnect();
        MongoCollection collection = mdb.getCollection("file_log");

        File folder = new File("C:\\Users\\hoang\\OneDrive\\Máy tính\\data warehouse\\saved");
        for (File f : folder.listFiles()) {

            try {
                String filePath = f.getCanonicalPath();
                String fileName = filePath.substring(filePath.lastIndexOf("\\") + 1);
                FTPClient ftpClient = new FTPClient();

                ftpClient.connect(server, port);
                ftpClient.login(user, pass);
                ftpClient.enterLocalPassiveMode();
                ftpClient.setFileType(FTP.BINARY_FILE_TYPE);

                // APPROACH #1: uploads first file using an InputStream
                File firstLocalFile = new File(filePath);
                System.out.println(firstLocalFile.exists());
                String firstRemoteFile = "/data/"+filePath.substring(filePath.lastIndexOf("\\")+1);

                InputStream inputStream = new FileInputStream(firstLocalFile);
                System.out.println("Start uploading first file");
                boolean done = ftpClient.storeFile(firstRemoteFile, inputStream);
                inputStream.close();
                if (done) {
                    System.out.println("The first file is uploaded successfully.");
                    Document query = new Document().append("file_name",  fileName);
                    Bson updates = Updates.combine(
                            Updates.set("status", "TF"));
                    UpdateOptions options = new UpdateOptions().upsert(true);
                    try {
                        UpdateResult result = collection.updateOne(query, updates, options);
                        System.out.println("Modified document count: " + result.getModifiedCount());
                        System.out.println("Upserted id: " + result.getUpsertedId()); // only contains a value when an upsert is performed
                    } catch (MongoException me) {
                        System.err.println("Unable to update due to an error: " + me);
                    }
                    f.delete();
                } else {
                    System.out.println("The first file is uploaded fail.");
                }

            } catch (IOException ex) {
                System.out.println("Error: " + ex.getMessage());
                ex.printStackTrace();
            }
        }


    }
}
