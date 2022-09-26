package dao;

import Bean.Config;
import db.DbConnector;
import org.jdbi.v3.core.statement.OutParameters;

import java.sql.Types;

public class FileDAO {
    private static FileDAO instance;

    public static FileDAO getInstance() {
        if (instance == null)
            instance = new FileDAO();
        return instance;
    }

    public Config getConfig(String sourceName) {
        OutParameters result = DbConnector.get().withHandle(h -> h.createCall("{call get_file_config(?,?,?,?,?,?)}")
                .bind(0, sourceName)
                .registerOutParameter(1, Types.INTEGER)
                .registerOutParameter(2, Types.VARCHAR)
                .registerOutParameter(3, Types.VARCHAR)
                .registerOutParameter(4, Types.VARCHAR)
                .registerOutParameter(5, Types.VARCHAR)
                .invoke());
        return new Config(result.getInt(1), sourceName, result.getString(2), result.getString(3), result.getString(4), result.getString(5));
    }

    public void insertFileLog(int idConfig, String fileName, String status, String author) {
        OutParameters result = DbConnector.get().withHandle(h -> h.createCall("{call insert_file_log(?,?,?,?)}")
                .bind(0, idConfig)
                .bind(1, fileName)
                .bind(2, status)
                .bind(3, author).invoke());
    }
    public void storeFileConfig(String fileName){
        OutParameters result = DbConnector.get().withHandle(h -> h.createCall("{call store_file_log(?)}")
                .bind(0, fileName)
                .invoke());
    }
    public void stagingFileConfig(String fileName){
        OutParameters result = DbConnector.get().withHandle(h -> h.createCall("{call staging_file_log(?)}")
                .bind(0, fileName)
                .invoke());
    }
}
