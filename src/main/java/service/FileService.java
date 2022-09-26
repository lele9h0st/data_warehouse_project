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
    public void insertFileLog(int idConfig, String fileName, String author){
         FileDAO.getInstance().insertFileLog(idConfig, fileName, "ER", author);
    }
    public void storeFileConfig(String fileName){
        FileDAO.getInstance().storeFileConfig(fileName);
    }
    public void stagingFileConfig(String fileName){
        FileDAO.getInstance().stagingFileConfig(fileName);
    }
}
