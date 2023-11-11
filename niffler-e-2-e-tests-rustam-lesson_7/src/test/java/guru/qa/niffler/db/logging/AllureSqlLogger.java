package guru.qa.niffler.db.logging;

import static org.apache.commons.lang3.StringUtils.isNotEmpty;

import com.github.vertical_blank.sqlformatter.SqlFormatter;
import com.github.vertical_blank.sqlformatter.languages.Dialect;
import com.p6spy.engine.logging.Category;
import com.p6spy.engine.spy.appender.StdoutLogger;
import io.qameta.allure.attachment.AttachmentData;
import io.qameta.allure.attachment.AttachmentProcessor;
import io.qameta.allure.attachment.AttachmentRenderer;
import io.qameta.allure.attachment.DefaultAttachmentProcessor;
import io.qameta.allure.attachment.FreemarkerAttachmentRenderer;

public class AllureSqlLogger extends StdoutLogger {

  private final AttachmentProcessor<AttachmentData> attachmentProcessor = new DefaultAttachmentProcessor();
  private final AttachmentRenderer<AttachmentData> attachmentRenderer = new FreemarkerAttachmentRenderer("sql-query.ftl");

  @Override
  public void logSQL(int connectionId, String now, long elapsed, Category category, String prepared,
      String sql, String url) {
    super.logSQL(connectionId, now, elapsed, category, prepared, sql, url);
    if (isNotEmpty(sql) && isNotEmpty(prepared)) {
//      SqlAttachment sqlAttachment = new SqlAttachment("SQL query", sql, prepared);
      SqlAttachment sqlAttachment = new SqlAttachment(
          "SQL query",
          SqlFormatter.of(Dialect.PostgreSql).format(sql),
          SqlFormatter.of(Dialect.PostgreSql).format(prepared)
      );
      attachmentProcessor.addAttachment(sqlAttachment, attachmentRenderer);
    }
  }
}
