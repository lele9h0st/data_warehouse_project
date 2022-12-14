package dao;

import db.DbConnector;
import org.jdbi.v3.core.statement.OutParameters;

public class StagingDAO {
    private static StagingDAO instance;

    public static StagingDAO getInstance() {
        if (instance == null)
            instance = new StagingDAO();
        return instance;
    }

    public void insertRecord(String province, String region, String date, String time, String temperature, String status, String low, String high, String humidity, String visibility, String wind, double uv, String air,String source) {
        OutParameters result= DbConnector.get().withHandle(h->h.createCall("{call insert_record1(:province, :date, :time, :temperature, :status, :low, :high, :humidity, :visibility, :wind, :uv, :air,:source)}")
                .bind("province",province)
                .bind("date",date)
                .bind("time",time)
                .bind("temperature",temperature)
                .bind("status",status)
                .bind("low",low)
                .bind("high",high)
                .bind("humidity",humidity)
                .bind("visibility",visibility)
                .bind("wind",wind)
                .bind("uv",uv)
                .bind("air",air)
                .bind("source",source).invoke()
        );
    }
    public void transfer_from_staging(){
        OutParameters result= DbConnector.get().withHandle(h->h.createCall("{call transfer_from_staging1()}").invoke());
    }

}
