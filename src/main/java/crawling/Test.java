package crawling;

import Bean.Config;
import dao.FileDAO;

public class Test {
    public static void main(String[] args) {
        Config c= FileDAO.getInstance().getConfig("thoitiet");
        System.out.println(c.getIp());
        System.out.println(c.getIdConfig());
        System.out.println(c.getPort());
        System.out.println(c.getSourceLoad());
        System.out.println(c.getSourceName());
    }
}
