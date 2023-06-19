package guru.qa.niffler.api.context;

import java.util.HashMap;
import java.util.Map;

public class SessionContext {

    private final Map<String, String> storage;
    private static final ThreadLocal<SessionContext> INSTANCE = ThreadLocal.withInitial(
            SessionContext::new);
    private static final String CODE_VERIFIER_KEY = "CODE_VERIFIER";
    private static final String CODE_CHALLENGE_KEY = "CODE_CHALLENGE";
    private static final String TOKEN_KEY = "TOKEN";
    private static final String CODE_KEY = "CODE";

    private SessionContext() {
        storage = new HashMap<>();
    }

    public static SessionContext getInstance() {
        return INSTANCE.get();
    }

    public String getCodeVerifier() {
        return storage.get(CODE_VERIFIER_KEY);
    }

    public String getCodeChallenge() {
        return storage.get(CODE_CHALLENGE_KEY);
    }

    public String getToken() {
        return storage.get(TOKEN_KEY);
    }

    public String getCode() {
        return storage.get(CODE_KEY);
    }

    public void setCodeVerifier(String codeVerifier) {
        storage.put(CODE_VERIFIER_KEY, codeVerifier);
    }

    public void setCodeChallenge(String codeChallenge) {
        storage.put(CODE_CHALLENGE_KEY, codeChallenge);
    }

    public void setToken(String token) {
        storage.put(TOKEN_KEY, token);
    }

    public void setCode(String code) {
        storage.put(CODE_KEY, code);
    }

    public void release() {
        storage.clear();
    }
}
