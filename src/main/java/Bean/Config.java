package Bean;

public class Config {
    private int idConfig;
    private String sourceName;
    private String sourceLoad;
    private String ip;
    private String v;
    private String port;

    public int getIdConfig() {
        return idConfig;
    }

    public void setIdConfig(int idConfig) {
        this.idConfig = idConfig;
    }

    public String getSourceName() {
        return sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

    public String getSourceLoad() {
        return sourceLoad;
    }

    public void setSourceLoad(String sourceLoad) {
        this.sourceLoad = sourceLoad;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getV() {
        return v;
    }

    public void setV(String v) {
        this.v = v;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public Config(int idConfig, String sourceName, String sourceLoad, String ip, String v, String port) {
        this.idConfig = idConfig;
        this.sourceName = sourceName;
        this.sourceLoad = sourceLoad;
        this.ip = ip;
        this.v = v;
        this.port = port;
    }
}
