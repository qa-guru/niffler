package guru.qa.niffler.hw1.enums;

public enum EndpointsData {

  SPEND_URL("http://127.0.0.1:8093/addSpend");

  public final String title;

  EndpointsData(String title) {
    this.title = title;
  }
}
