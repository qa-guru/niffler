package guru.qa.niffler.api.service;

import javax.annotation.ParametersAreNonnullByDefault;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.CookieStore;
import java.net.HttpCookie;
import java.net.URI;
import java.util.List;

@ParametersAreNonnullByDefault
public enum ThreadLocalCookieStore implements CookieStore {
  INSTANCE;

  private final ThreadLocal<CookieStore> store = ThreadLocal.withInitial(
      ThreadLocalCookieStore::inMemoryCookieStore
  );

  @Override
  public void add(URI uri, HttpCookie cookie) {
    getStore().add(uri, cookie);
  }

  @Override
  public List<HttpCookie> get(URI uri) {
    return getStore().get(uri);
  }

  @Override
  public List<HttpCookie> getCookies() {
    return getStore().getCookies();
  }

  @Override
  public List<URI> getURIs() {
    return getStore().getURIs();
  }

  @Override
  public boolean remove(URI uri, HttpCookie cookie) {
    return getStore().remove(uri, cookie);
  }

  @Override
  public boolean removeAll() {
    return getStore().removeAll();
  }

  private CookieStore getStore() {
    return store.get();
  }

  public String cookieValue(String cookieName) {
    return getCookies().stream()
        .filter(c -> c.getName().equals(cookieName))
        .map(HttpCookie::getValue)
        .findFirst()
        .orElseThrow();
  }

  private static CookieStore inMemoryCookieStore() {
    return new CookieManager(null, CookiePolicy.ACCEPT_ALL).getCookieStore();
  }
}