package Bean;

public class Weather {
    private String province;
    private String region;
    private String date;
    private String time;
    private String temperature;
    private String status;
    private String low;
    private String high;
    private String humidity;
    private String visibility;
    private String wind;
    private double uv;
    private String air;

    public Weather(String province, String region, String date, String time, String temperature, String status, String low, String high, String humidity, String visibility, String wind, double uv, String air) {
        this.province = province;
        this.region = region;
        this.date = date;
        this.time = time;
        this.temperature = temperature;
        this.status = status;
        this.low = low;
        this.high = high;
        this.humidity = humidity;
        this.visibility = visibility;
        this.wind = wind;
        this.uv = uv;
        this.air = air;
    }
    public Weather(String[] params){
        this.province = params[0];
        this.region = params[1];
        this.date = params[2];
        this.time = params[3];
        this.temperature = params[4];
        this.status = params[5];
        this.low = params[6];
        this.high = params[7];
        this.humidity = params[8];
        this.visibility = params[9];
        this.wind = params[10];
        this.uv = Double.parseDouble(params[11]);
        this.air = params[12];
    }
}
