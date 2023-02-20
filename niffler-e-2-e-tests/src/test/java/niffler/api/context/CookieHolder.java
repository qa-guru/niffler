package niffler.api.context;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CookieHolder {

    private static final String COOKIE_KEY = "Cookie";

    private final Map<String, List<String>> cookieStorage;

    private static final ThreadLocal<CookieHolder> INSTANCE = ThreadLocal.withInitial(CookieHolder::new);

    private CookieHolder() {
        cookieStorage = new HashMap<>(Map.of(COOKIE_KEY, new ArrayList<>()));
    }

    public static CookieHolder getInstance() {
        return INSTANCE.get();
    }

    public void setCookie(List<String> value) {
        cookieStorage.put(COOKIE_KEY, value);
    }

    public void removeCookie(String cookiePart) {
        getAll().get(COOKIE_KEY).removeIf(c -> c.contains(cookiePart));
    }

    public String getCookieByPart(String cookiePart) {
        List<String> cookies = getAll().get(COOKIE_KEY);
        for (String cookie : cookies) {
            if (cookie.contains(cookiePart))
                return cookie;
        }
        throw new IllegalArgumentException("No cookie for part " + cookiePart);
    }

    public String getCookieValueByPart(String cookiePart) {
        return getCookieByPart(cookiePart).split("=")[1];
    }

    public List<String> getStoredCookies() {
        return cookieStorage.get(COOKIE_KEY);
    }

    public Map<String, List<String>> getAll() {
        return cookieStorage;
    }

    public void flushAll() {
        cookieStorage.get(COOKIE_KEY).clear();
    }
}
