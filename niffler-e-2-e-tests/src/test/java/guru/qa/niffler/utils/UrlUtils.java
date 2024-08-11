package guru.qa.niffler.utils;

import javax.annotation.Nullable;
import java.net.URI;
import java.net.URL;

public class UrlUtils {

  public static boolean isValidURL(@Nullable String urlString) {
    try {
      URL url = new URI(urlString).toURL();
      url.toURI();
      return true;
    } catch (Exception e) {
      return false;
    }
  }
}
