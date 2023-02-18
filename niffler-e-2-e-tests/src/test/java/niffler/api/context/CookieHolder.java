package niffler.api.context;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CookieHolder {

    private final Map <String, List<String>> cookieStorage;

    private static final ThreadLocal<CookieHolder> INSTANCE = ThreadLocal.withInitial(CookieHolder::new);

    private CookieHolder() {
        cookieStorage = new HashMap<>(Map.of("Cookie", new ArrayList<>()));
    }

    public static CookieHolder getInstance() {
        return INSTANCE.get();
    }

    public void setCookie(List<String> value) {
        cookieStorage.put("Cookie", value);
    }

    public void removeCookie(String cookiePart) {
        getAll().get("Cookie").removeIf(c -> c.contains(cookiePart));
    }

    public String getCookieByPart(String cookiePart) {
        List<String> cookies = getAll().get("Cookie");
        for (String cookie : cookies) {
            if (cookie.contains(cookiePart))
                return cookie;
        }
        return null;
    }

    public String getCookieValueByPart(String cookiePart) {
        return getCookieByPart(cookiePart).split("=")[1];
    }

    public List<String> getStoredCookies() {
        return cookieStorage.get("Cookie");
    }
    public Map<String, List<String>> getAll() {
        return cookieStorage;
    }
}
