package guru.qa.niffler.data.logging;

import io.qameta.allure.attachment.AttachmentData;
import lombok.Getter;
import javax.annotation.ParametersAreNonnullByDefault;

@Getter
@ParametersAreNonnullByDefault
public class SqlRequestAttachment implements AttachmentData {
  private final String name;
  private final String sql;

  public SqlRequestAttachment(String name, String sql) {
    this.name = name;
    this.sql = sql;
  }
}