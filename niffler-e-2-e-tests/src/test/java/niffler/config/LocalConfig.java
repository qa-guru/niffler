package niffler.config;

public class LocalConfig implements Config {
    @Override
    public String frontUrl() {
        return "http://127.0.0.1:3000/";
    }
}
