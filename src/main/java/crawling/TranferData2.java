package crawling;


import Bean.Config;
import com.mongodb.MongoException;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.UpdateResult;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.bson.Document;
import org.bson.conversions.Bson;
import service.FileService;
import service.StagingService;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TranferData2 {
    static String server ;
    static int port ;
    static String user = "hoang";
    static String pass = "";
    static String localFolder="C:\\Users\\hoang\\OneDrive\\Máy tính\\data warehouse\\temp\\";
    static String serverFolder1="/data2/";
    public static boolean downloadFileFromServer(String fileName,FTPClient ftpClient) throws IOException {
        String remoteFile = serverFolder1+fileName;
        String localFile=localFolder+fileName;
        OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(localFile));
        boolean success = ftpClient.retrieveFile(remoteFile, outputStream);
        outputStream.close();
        return success;
    }
    public static void staging(File f) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(f)));
        String line = in.readLine();
        while (line != null) {
            String[] data = line.split(",");
            StagingService.getInstance().insertRecord(data);
            line = in.readLine();
        }
        in.close();
        f.delete();
//        FileService.getInstance().stagingFileConfig(f.getName());
    }
    public static void main(String[] args) throws IOException {
        Config config= FileService.getInstance().getConfig("thoitiet");
        server= config.getIp();
        port=Integer.parseInt(config.getPort());
        File folder = new File("C:\\Users\\hoang\\OneDrive\\Máy tính\\data warehouse\\weather");
        FTPClient ftpClient = new FTPClient();
        ftpClient.connect(server, port);
        ftpClient.login(user, pass);
        ftpClient.enterLocalPassiveMode();
        ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
        FTPFile[] files = ftpClient.listFiles("/data2");
        for(FTPFile f:files){
            downloadFileFromServer(f.getName(),ftpClient);
            File fileTemp=new File(localFolder+f.getName());
            staging(fileTemp);
        }
    }
}

