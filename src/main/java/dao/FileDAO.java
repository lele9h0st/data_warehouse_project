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
        OutParameters result = DbConnector.get().withHandle(h -> h.createCall("{call get_file_config(?,?,?,?,?,?,?,?)}")
                .bind(0, sourceName)
                .registerOutParameter(1, Types.INTEGER)
                .registerOutParameter(2, Types.VARCHAR)
                .registerOutParameter(3, Types.VARCHAR)
                .registerOutParameter(4, Types.VARCHAR)
                .registerOutParameter(5, Types.VARCHAR)
                .registerOutParameter(6, Types.VARCHAR)
                .registerOutParameter(7, Types.VARCHAR)
                .invoke());
        return new Config(result.getInt(1), sourceName, result.getString(2), result.getString(3), result.getString(4), result.getString(5), result.getString(6), result.getString(7));
    }

    public void insertFileLog(String  source, String fileName, String status, String author) {
        OutParameters result = DbConnector.get().withHandle(h -> h.createCall("{call insert_file_log(?,?,?,?)}")
                .bind(0, source)
                .bind(1, fileName)
                .bind(2, status)
                .bind(3, author).invoke());
    }

    public void storeFileConfig(String fileName) {
        OutParameters result = DbConnector.get().withHandle(h -> h.createCall("{call store_file_log(?)}")
                .bind(0, fileName)
                .invoke());
    }

    public void stagingFileConfig(String fileName) {
        OutParameters result = DbConnector.get().withHandle(h -> h.createCall("{call staging_file_log(?)}")
                .bind(0, fileName)
                .invoke());
    }

    public void finishCrawlFileLog(String fileName) {
        OutParameters result = DbConnector.get().withHandle(h -> h.createCall("{call finish_crawl_file_log(?)}")
                .bind(0, fileName)
                .invoke());
    }

    public void errorCrawlFileLog(String fileName) {
        OutParameters result = DbConnector.get().withHandle(h -> h.createCall("{call error_crawl_file_log(?)}")
                .bind(0, fileName)
                .invoke());
    }
    public void errorTranferStagingFile(String fileName) {
        OutParameters result = DbConnector.get().withHandle(h -> h.createCall("{call staging_tranfer_file_error_log(?)}")
                .bind(0, fileName)
                .invoke());
    }
    public void errorTranferWarehouseFile(String fileName) {
        OutParameters result = DbConnector.get().withHandle(h -> h.createCall("{call warehouse_tranfer_file_error_log(?)}")
                .bind(0, fileName)
                .invoke());
    }
    public int countERFile() {
        OutParameters result = DbConnector.get().withHandle(h -> h.createCall("{call file_count_in_server(:count)}")
                .registerOutParameter("count", Types.INTEGER)
                .invoke());
        return result.getInt("count");
    }
}
