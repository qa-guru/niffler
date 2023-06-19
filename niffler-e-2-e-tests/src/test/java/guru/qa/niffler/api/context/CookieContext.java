package guru.qa.niffler.api.context;

import java.util.HashMap;
import java.util.Map;

public class CookieContext {

    private final Map<String, String> storage;
    private static final ThreadLocal<CookieContext> INSTANCE = ThreadLocal.withInitial(
            CookieContext::new);

    private CookieContext() {
        storage = new HashMap<>();
    }

    public static CookieContext getInstance() {
        return INSTANCE.get();
    }


    public void setCookie(String key, String cookie) {
        storage.put(key, cookie);
    }

    public String getCookie(String key) {
        return storage.get(key);
    }

    public void removeCookie(String key) {
        storage.remove(key);
    }

    public String getFormattedCookie(String key) {
        return key + "=" + getCookie(key);
    }

    public void release() {
        storage.clear();
    }
}
