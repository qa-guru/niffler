package niffler.api.context;

import niffler.api.utils.LoginUtils;

import java.util.HashMap;
import java.util.Map;

public class SessionStorage {
    private final Map<String,String> sessionStorage;

    private static final ThreadLocal<SessionStorage> INSTANCE = ThreadLocal.withInitial(SessionStorage::new);

    private SessionStorage() {
        sessionStorage = new HashMap<>();
    }

    public static SessionStorage getInstance() {
        return INSTANCE.get();
    }

    public void init() {
        final String codeVerifier = LoginUtils.generateCodeVerifier();
        sessionStorage.put("codeVerifier", codeVerifier);
        sessionStorage.put("codeChallenge", LoginUtils.generateCodeChallange(codeVerifier));

    }

    public void addCode(String code) {
        sessionStorage.put("code", code);
    }

    public String getCode() {
        return sessionStorage.get("code");
    }

    public void addToken(String token) {
        sessionStorage.put("id_token", token);
    }

    public String getToken() {
        return sessionStorage.get("id_token");
    }

    public String getCodeVerifier() {
        return sessionStorage.get("codeVerifier");
    }

    public String getCodeChallenge() {
        return sessionStorage.get("codeChallenge");
    }
}
