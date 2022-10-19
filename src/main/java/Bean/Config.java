package Bean;

public class Config {
    private int idConfig;
    private String sourceName;
    private String sourceLoad;
    private String ip;
    private String v;
    private String port;
    private String username;
    private String password;

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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Config(int idConfig, String sourceName, String sourceLoad, String ip, String v, String port, String username, String password) {
        this.idConfig = idConfig;
        this.sourceName = sourceName;
        this.sourceLoad = sourceLoad;
        this.ip = ip;
        this.v = v;
        this.port = port;
        this.username = username;
        this.password = password;
    }
}
