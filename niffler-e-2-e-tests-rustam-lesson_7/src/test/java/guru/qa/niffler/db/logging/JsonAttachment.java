package guru.qa.niffler.db.logging;

import guru.qa.niffler.model.Spend7Json;
import io.qameta.allure.attachment.AttachmentData;
import java.io.InputStream;

public class JsonAttachment implements AttachmentData {

  private final String name;
  private final Spend7Json text;

  public JsonAttachment(String name, Spend7Json text) {
    this.name = name;
    this.text = text;
  }

  @Override
  public String getName() {
    return name;
  }

  public Spend7Json getText() {
    return text;
  }

}
