package niffler.config;

public class DockerConfig implements Config {
    @Override
    public String frontUrl() {
        return "http://127.0.0.1/";
    }
}
