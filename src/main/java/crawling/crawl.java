package crawling;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.InsertOneResult;
import db.MongoDBConnector;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class crawl {

    public static void main(String[] args) throws IOException, InterruptedException {
        MongoDatabase mdb = MongoDBConnector.makeConnect();
        MongoCollection collection = mdb.getCollection("file_log");
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\hoang\\OneDrive\\Máy tính\\data warehouse\\chromedriver.exe");
        WebDriver driver = new ChromeDriver();
        driver.get("https://kqxs.vn/");

        String ngay = "";
        WebElement date = driver.findElement(By.className("today"));
        WebElement mienBac = driver.findElement(By.id("mien-bac"));
        WebElement mienTrung = driver.findElement(By.id("mien-trung"));
        WebElement mienNam = driver.findElement(By.id("mien-nam"));
        String s = mienBac.findElement(By.tagName("thead")).getText();
        String[] sList = s.split(" ");
        ngay = String.join(" ", Arrays.asList(Arrays.copyOfRange(sList, sList.length - 1, sList.length)));
        String nameFile = "kqxs.com.daily." + ngay + ".csv";
        File f = new File("C:\\Users\\hoang\\OneDrive\\Máy tính\\data warehouse\\saved" + "\\" + nameFile);
        f.createNewFile();
        OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(f), "UTF-8");
        WebElement[] miens = new WebElement[]{mienTrung, mienNam, mienBac};
        for (WebElement mien : miens) {
            List<WebElement> tr = mien.findElements(By.tagName("tbody")).get(0).findElements(By.tagName("tr"));
            s = mien.findElement(By.tagName("thead")).getText();
            sList = s.split(" ");
            String tenDai = String.join(" ", Arrays.asList(Arrays.copyOfRange(sList, 0, sList.length - 3)));
            ngay = String.join(" ", Arrays.asList(Arrays.copyOfRange(sList, sList.length - 1, sList.length)));
            String line = "";
            System.out.println(tr.size());
            List<String> daiS = new ArrayList<>();
            if (!mien.equals(mienBac)) {
                List<WebElement> dai = tr.get(0).findElements(By.className("wrap-text"));
                for (WebElement d : dai) {
                    daiS.add(d.getText());
//                System.out.println(d.getText());
                }
            }
            for (int i = 1; i < tr.size(); i++) {
                String prize = tr.get(i).findElement(By.className("prize")).getText();
                List<WebElement> results = tr.get(i).findElements(By.className("number"));
                for (int j = 0; j < results.size(); j++) {
                    if (!mien.equals(mienBac))
                        tenDai = "Xổ số " + daiS.get(j % daiS.size());
                    line = tenDai + "," + ngay + "," + prize + "," + results.get(j).getText();
                    System.out.println(line);
                    out.write(line + "\n");
                }
            }
        }
        InsertOneResult result = collection.insertOne(new Document().append("_id", new ObjectId()).append("id_config", 1).append("file_name",nameFile).append("date", ngay).append("status", "CR").append("author", "hoang"));
        System.out.println("Success! Inserted document id: " + result.getInsertedId());
        out.close();
        driver.quit();
    }
}
