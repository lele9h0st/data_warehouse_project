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

public class TranferData {
//     static String server ;
//    static int port ;
//    static String user = "hoang";
//    static String pass = "";
//    public static void staging(File f) throws IOException {
//        BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(f)));
//        String line = in.readLine();
//        while (line != null) {
//            String[] data = line.split(",");
//            StagingService.getInstance().insertRecord(data);
//            line = in.readLine();
//        }
//        in.close();
//        FileService.getInstance().stagingFileConfig(f.getName());
//    }
//    public static void uploadFileToFTPServer(File f){
//
//        try {
//            String filePath = f.getCanonicalPath();
//            String fileName = filePath.substring(filePath.lastIndexOf("\\") + 1);
//            FTPClient ftpClient = new FTPClient();
//
//            ftpClient.connect(server, port);
//            ftpClient.login(user, pass);
//            ftpClient.enterLocalPassiveMode();
//            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
//
//            // APPROACH #1: uploads first file using an InputStream
//            File firstLocalFile = new File(filePath);
//            System.out.println(firstLocalFile.exists());
//            String firstRemoteFile = "/data2/"+filePath.substring(filePath.lastIndexOf("\\")+1);
//
//            InputStream inputStream = new FileInputStream(firstLocalFile);
//            System.out.println("Start uploading first file");
//            boolean done = ftpClient.storeFile(firstRemoteFile, inputStream);
//
//            inputStream.close();
//            if (done) {
//                System.out.println("file is uploaded successfully.");
//                FileService.getInstance().storeFileConfig(f.getName());
////                f.delete();
//            } else {
//                System.out.println(" file is uploaded fail.");
//            }
//
//        } catch (IOException ex) {
//            System.out.println("Error: " + ex.getMessage());
//            ex.printStackTrace();
//        }
//    }
//    public static void main(String[] args) throws IOException {
//        Config config= FileService.getInstance().getConfig("thoitiet");
//        server= config.getIp();
//        port=Integer.parseInt(config.getPort());
//        File folder = new File("C:\\Users\\hoang\\OneDrive\\Máy tính\\data warehouse\\weather");
//        for (File f : folder.listFiles()) {
//            staging(f);
//            StagingService.getInstance().transfer_from_staging();
//            uploadFileToFTPServer(f);
//        }
//    }
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
//            StagingService.getInstance().insertRecord(data);
            line = in.readLine();
        }
        in.close();
        uploadFileToFTPServer(f);
        f.delete();
        FileService.getInstance().stagingFileConfig(f.getName());
    }
    public static void uploadFileToFTPServer(File f){

        try {
            String filePath = f.getCanonicalPath();
            FTPClient ftpClient = new FTPClient();

            ftpClient.connect(server, port);
            ftpClient.login(user, pass);
            ftpClient.enterLocalPassiveMode();
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);

            // APPROACH #1: uploads first file using an InputStream
            File firstLocalFile = new File(filePath);
            System.out.println(firstLocalFile.exists());
            String firstRemoteFile = "/staged/"+filePath.substring(filePath.lastIndexOf("\\")+1);

            InputStream inputStream = new FileInputStream(firstLocalFile);
            System.out.println("Start uploading first file");
            boolean done = ftpClient.storeFile(firstRemoteFile, inputStream);

            inputStream.close();
            if (done) {
                System.out.println("file is uploaded successfully.");
                FileService.getInstance().storeFileConfig(f.getName());
            } else {
                System.out.println(" file is uploaded fail.");
            }

        } catch (IOException ex) {
            System.out.println("Error: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
    public static void main(String[] args) throws IOException {
        Config config= FileService.getInstance().getConfig("thoitiet");
        server= config.getIp();
        port=Integer.parseInt(config.getPort());
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
            ftpClient.deleteFile(serverFolder1+f.getName());
            StagingService.getInstance().transfer_from_staging();
        }

    }
}
