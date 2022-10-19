package service;

import Bean.Config;
import dao.FileDAO;

public class FileService {
    private static FileService instance;

    public static FileService getInstance() {
        if (instance == null)
            instance = new FileService();
        return instance;
    }
    public Config getConfig(String sourceName){
        return FileDAO.getInstance().getConfig(sourceName);
    }
    public void insertFileLog(String  source, String fileName,String status, String author){
         FileDAO.getInstance().insertFileLog(source, fileName, status, author);
    }
    public void finishCrawlFileLog( String fileName){
        FileDAO.getInstance().finishCrawlFileLog( fileName);
    }
    public void errorCrawlFileLog( String fileName){
        FileDAO.getInstance().errorCrawlFileLog( fileName);
    }
    public void storeFileConfig(String fileName){
        FileDAO.getInstance().storeFileConfig(fileName);
    }
    public void stagingFileConfig(String fileName){
        FileDAO.getInstance().stagingFileConfig(fileName);
    }
}
