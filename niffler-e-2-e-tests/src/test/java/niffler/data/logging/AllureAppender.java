package niffler.data.logging;

import com.github.vertical_blank.sqlformatter.SqlFormatter;
import com.github.vertical_blank.sqlformatter.languages.Dialect;
import com.p6spy.engine.logging.Category;
import com.p6spy.engine.spy.appender.StdoutLogger;
import io.qameta.allure.Allure;
import io.qameta.allure.attachment.AttachmentData;
import io.qameta.allure.attachment.AttachmentProcessor;
import io.qameta.allure.attachment.DefaultAttachmentProcessor;
import io.qameta.allure.attachment.FreemarkerAttachmentRenderer;

import static org.apache.commons.lang3.StringUtils.isNotEmpty;

public class AllureAppender extends StdoutLogger {
    private final AttachmentProcessor<AttachmentData> processor = new DefaultAttachmentProcessor();
    private final String sqlTemplatePath = "sql-query.ftl";

    @Override
    public void logSQL(int connectionId, String now, long elapsed, Category category,
                       String prepared, String sql, String url) {
        super.logSQL(connectionId, now, elapsed, category, prepared, sql, url);
        if (isNotEmpty(prepared) && isNotEmpty(sql)) {
            SqlRequestAttachment attachment = new SqlRequestAttachment(
                    "SQL statement and query",
                    SqlFormatter.of(Dialect.StandardSql).format(prepared),
                    SqlFormatter.of(Dialect.StandardSql).format(sql));
            processor.addAttachment(attachment, new FreemarkerAttachmentRenderer(sqlTemplatePath));
        }
    }

    @Override
    public void logException(Exception e) {
        super.logException(e);
        Allure.addAttachment("Exception stacktrace", e.getMessage());
    }

    @Override
    public void logText(String sql) {
        super.logText(sql);
    }

    @Override
    public boolean isCategoryEnabled(Category category) {
        return true;
    }
}
