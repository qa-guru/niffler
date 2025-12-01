package guru.qa.niffler.config;

public interface Callbacks {

  interface Android {
    String login = "/callback";
    String logout = "/logout_callback";
    String init = "/start";
  }

  interface Web {
    String login = "/authorized";
    String logout = "/logout";
    String init = "/main";
  }
}
