package crawling;

import Bean.Config;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import service.FileService;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class crawl2 {


    public static void main(String[] args) throws IOException {
        String server;
        int port;
        String user = "hoang";
        String pass = "";
        Config config = FileService.getInstance().getConfig("thoitiet");
        server = config.getIp();
        port = Integer.parseInt(config.getPort());


        Document main = null;
        try {
            main = Jsoup.connect(config.getSourceLoad()).get();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        String timer = main.getElementById("timer").text();
        String time = timer.substring(0, timer.lastIndexOf("|")).trim();
        String savedTime = time.replace(":", "-");
        String date = timer.substring(timer.lastIndexOf("|") + 1).trim();
        String savedDate = date.replace("/", "-");
        List<Element> menu = main.getElementsByClass("col-megamenu");
        String nameFile = "thoitiet.vn." + savedDate + "." + savedTime + ".csv";
        String filePath = "C:\\Users\\hoang\\OneDrive\\Máy tính\\data warehouse\\weather" + "\\" + nameFile;
        File f = new File(filePath);
        FileService.getInstance().insertFileLog(config.getIdConfig(), nameFile, "ES", "hoang");
        OutputStreamWriter out;
        try {
            f.createNewFile();
            out = new OutputStreamWriter(new FileOutputStream(f), "UTF-8");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        String line = "";
        boolean isError = false;
        try {
            for (Element region : menu) {
                String nameRegion = region.getElementsByClass("title").get(0).text();
                List<Element> listProvince = region.getElementsByTag("li");
                for (Element p : listProvince) {
                    String href = p.getElementsByTag("a").attr("href");
                    Document document = Jsoup.connect("https://thoitiet.vn" + href).get();
                    String nameLocation = p.getElementsByTag("a").attr("title");
                    List<Element> currentLocations = document.getElementsByClass("current-location");
                    Element mainBlock = currentLocations.get(0);
                    String currentTemp = mainBlock.getElementsByClass("current-temperature").get(0).text();
                    currentTemp = currentTemp.substring(0, currentTemp.length() - 1);
                    String overview = mainBlock.getElementsByClass("overview-caption-item-detail").get(0).text();

                    Element weatherDetail = mainBlock.getElementsByClass("weather-detail").get(0);
                    List<Element> detailList = weatherDetail.select(":root > div");
                    String highLowTemp = detailList.get(0).select(":root > div").get(1).getElementsByTag("span").get(1).text();

                    String lowTemp = highLowTemp.substring(0, highLowTemp.lastIndexOf("/")).trim();
                    lowTemp = lowTemp.substring(0, lowTemp.length() - 1);
                    String highTemp = highLowTemp.substring(highLowTemp.lastIndexOf("/") + 1).trim();
                    highTemp = highTemp.substring(0, highTemp.length() - 1);
                    String humidity = detailList.get(1).select(":root > div").get(1).getElementsByTag("span").get(1).text();
                    humidity = humidity.substring(0, humidity.length() - 1);
                    String visibility = detailList.get(2).select(":root > div").get(1).getElementsByTag("span").get(1).text().split(" ")[0];

                    String wind = detailList.get(3).select(":root > div").get(1).getElementsByTag("span").get(1).text().split(" ")[0];
                    String UVLevel = detailList.get(5).select(":root > div").get(1).getElementsByTag("span").get(1).text();


                    String airTitle = document.getElementsByClass("air-title").get(0).text();
                    String airQuality = airTitle.substring(airTitle.lastIndexOf(":") + 1).trim();
                    line = nameLocation + "," + nameRegion + "," + date + "," + time + "," + currentTemp + "," + overview + "," + lowTemp + "," + highTemp + "," + humidity + "," + visibility + "," + wind + "," + UVLevel + "," + airQuality;
                    out.write(line + "\n");
                    System.out.println(line);
                }
            }

            out.close();
        } catch (IOException e) {
            isError = true;
        }
        if (isError) {
            FileService.getInstance().errorCrawlFileLog(nameFile);
        } else {
            FileService.getInstance().finishCrawlFileLog(nameFile);
            FTPClient ftpClient = new FTPClient();

            ftpClient.connect(server, port);
            ftpClient.login(user, pass);
            ftpClient.enterLocalPassiveMode();
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
            String remoteFile = "/data2/" + nameFile;
            InputStream inputStream = new FileInputStream(f);
            System.out.println("Start uploading first file");
            boolean done = ftpClient.storeFile(remoteFile, inputStream);
            inputStream.close();
            if (done) {
                System.out.println("The file is uploaded successfully.");
                f.delete();
            }

        }
    }
}
