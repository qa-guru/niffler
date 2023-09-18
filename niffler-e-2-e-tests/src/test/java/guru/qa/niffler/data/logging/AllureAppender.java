package guru.qa.niffler.data.logging;

import com.github.vertical_blank.sqlformatter.SqlFormatter;
import com.github.vertical_blank.sqlformatter.languages.Dialect;
import com.p6spy.engine.logging.Category;
import com.p6spy.engine.spy.appender.StdoutLogger;
import io.qameta.allure.attachment.AttachmentData;
import io.qameta.allure.attachment.AttachmentProcessor;
import io.qameta.allure.attachment.DefaultAttachmentProcessor;
import io.qameta.allure.attachment.FreemarkerAttachmentRenderer;
import org.apache.commons.lang3.StringUtils;

import static org.apache.commons.lang3.StringUtils.isNotEmpty;

public class AllureAppender extends StdoutLogger {
    private final AttachmentProcessor<AttachmentData> processor = new DefaultAttachmentProcessor();
    private final String sqlTemplatePath = "sql-query.ftl";

    @Override
    public void logSQL(int connectionId, String now, long elapsed, Category category,
                       String prepared, String sql, String url) {
        if (isNotEmpty(sql)) {
            SqlRequestAttachment attachment = new SqlRequestAttachment(
                    sql.split("\\s+")[0] + " query [" + StringUtils.substringBefore(url, "?") + "]",
                    SqlFormatter.of(Dialect.StandardSql).format(sql));
            processor.addAttachment(attachment, new FreemarkerAttachmentRenderer(sqlTemplatePath));
        }
    }
}
