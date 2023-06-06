package guru.qa.niffler.db.logging;


import io.qameta.allure.attachment.AttachmentData;

public class SqlAttachment implements AttachmentData {

  private final String name;
  private final String sql;
  private final String statement;

  public SqlAttachment(String name, String sql, String statement) {
    this.name = name;
    this.sql = sql;
    this.statement = statement;
  }

  public String getSql() {
    return sql;
  }

  public String getStatement() {
    return statement;
  }

  @Override
  public String getName() {
    return name;
  }
}
