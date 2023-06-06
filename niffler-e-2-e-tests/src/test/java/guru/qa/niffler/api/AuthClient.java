package guru.qa.niffler.api;

public interface AuthClient {

  void authorizePreRequest();

  void login(String username, String password);

  String getToken();
}
