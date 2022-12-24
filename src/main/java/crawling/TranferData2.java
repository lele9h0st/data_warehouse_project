package crawling;


import Bean.Config;
import com.mongodb.MongoException;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.UpdateResult;
import dao.FileDAO;
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
    static String server;
    static int port;
    static String user;
    static String pass;
    static String localFolder = "C:\\Users\\hoang\\OneDrive\\Máy tính\\data warehouse\\temp\\";
    static String serverFolder1 = "/data/data";

    public static boolean downloadFileFromServer(String fileName, FTPClient ftpClient) throws IOException {
        String remoteFile = serverFolder1+"/" + fileName;
        String localFile = localFolder + fileName;
        File localF = new File(localFile);
        if (!localF.exists())
            localF.createNewFile();
        OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(localF));
        boolean success = ftpClient.retrieveFile(remoteFile, outputStream);
        outputStream.close();
        return success;
    }

    public static void staging(File f)  {
        try {
        BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(f)));
        String line = in.readLine();
        String fileName=f.getName();
        String source=fileName.substring(0,fileName.length()-21);
        System.out.println("filename:"+source);
        while (line != null) {
            String[] data = line.split(",");
            StagingService.getInstance().insertRecord(data,source);
            line = in.readLine();
        }
        in.close();
        uploadFileToFTPServer(f);
        System.out.println("isdeleted:"+f.delete());
        FileService.getInstance().stagingFileConfig(f.getName());
        }catch (Exception e){
            FileDAO.getInstance().errorTranferStagingFile(f.getName());
        }
    }

    public static void uploadFileToFTPServer(File f) {

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
            String firstRemoteFile = "/data/staged/" + filePath.substring(filePath.lastIndexOf("\\") + 1);

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
        Config config = FileService.getInstance().getConfig("thoitiet.vn");
        server = config.getIp();
        port = Integer.parseInt(config.getPort());
        user = config.getUsername();
        pass = config.getPassword();
        FTPClient ftpClient = new FTPClient();
        ftpClient.connect(server, port);
        ftpClient.login(user, pass);
        ftpClient.enterLocalPassiveMode();
        ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
        FTPFile[] files = ftpClient.listFiles(serverFolder1);
        System.out.println(files.length);
        for (FTPFile f : files) {
            if (!f.getName().equals(".") && !f.getName().equals("..")) {
                System.out.println(f.getName());
                downloadFileFromServer(f.getName(), ftpClient);
                File fileTemp = new File(localFolder + f.getName());

                staging(fileTemp);
                ftpClient.deleteFile(serverFolder1+"/" + f.getName());
                FileService.getInstance().insertFileLog(f.getName().substring(0,f.getName().length()-21), f.getName(), "TF", "hoang");
                try {
                    StagingService.getInstance().transfer_from_staging();
                }catch (Exception e){
                    FileDAO.getInstance().errorTranferWarehouseFile(f.getName());
                }


            }
        }

    }
}

