package crawling;

import Bean.Config;
import dao.FileDAO;
import db.DbConnector;
import service.StagingService;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class Test {
    public static void main(String[] args) throws IOException {
        String f = "C:\\Users\\hoang\\OneDrive\\Máy tính\\data warehouse\\Date_Dim\\date_dim_without_quarter.csv";
        BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(f)));
        String line = in.readLine();
        while (line != null) {
//            System.out.println(line);
            String[] data = line.split(",");
//            System.out.println(data.length);
            DbConnector.get().withHandle(h ->
                    h.createUpdate("INSERT INTO date_dim(date_sk,full_date,day_since_2005,month_since_2005,day_of_week,calendar_month,calendar_year,calendar_year_month,day_of_month,day_of_year,week_of_year_sunday,year_week_sunday,week_sunday_start,week_of_year_monday,year_week_monday,week_monday_start,holiday,day_type) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)")
                            .bind(0, data[0])
                            .bind(1, data[1])
                            .bind(2, data[2])
                            .bind(3, data[3])
                            .bind(4, data[4])
                            .bind(5, data[5])
                            .bind(6, data[6])
                            .bind(7, data[7])
                            .bind(8, data[8])
                            .bind(9, data[9])
                            .bind(10, data[10])
                            .bind(11, data[11])
                            .bind(12, data[12])
                            .bind(13, data[13])
                            .bind(14, data[14])
                            .bind(15, data[15])
                            .bind(16, data[16])
                            .bind(17, data[17])
                            .execute()
            );
            line = in.readLine();
        }
        in.close();
    }
}
