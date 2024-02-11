package guru.qa.niffler.api.logging;

import io.qameta.allure.attachment.AttachmentData;

public final class ReceivedCookieRespAttachment implements AttachmentData {

  public static ReceivedCookieRespAttachment newInstance(String name, String value) {
    return new ReceivedCookieRespAttachment(name, value);
  }

  private final String name, value;

  private ReceivedCookieRespAttachment(String name, String value) {
    this.name = name;
    this.value = value;
  }

  @Override
  public String getName() {
    return name;
  }

  public String getValue() {
    return value;
  }

}
