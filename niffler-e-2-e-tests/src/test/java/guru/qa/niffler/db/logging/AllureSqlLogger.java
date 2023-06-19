package guru.qa.niffler.db.logging;

import com.p6spy.engine.logging.Category;
import com.p6spy.engine.spy.appender.StdoutLogger;
import io.qameta.allure.attachment.AttachmentData;
import io.qameta.allure.attachment.AttachmentProcessor;
import io.qameta.allure.attachment.DefaultAttachmentProcessor;
import io.qameta.allure.attachment.FreemarkerAttachmentRenderer;

import static org.apache.commons.lang3.StringUtils.isNotEmpty;

public class AllureSqlLogger extends StdoutLogger {

    private final AttachmentProcessor<AttachmentData> attachmentProcessor = new DefaultAttachmentProcessor();
    private final String templatePath = "sql-query.ftl";

    @Override
    public void logSQL(int connectionId, String now, long elapsed, Category category, String prepared,
                       String sql, String url) {

        super.logSQL(connectionId, now, elapsed, category, prepared, sql, url);

        if (isNotEmpty(sql)) {
            SqlAttachment sqlAttachment = new SqlAttachment("sql attacment", sql, prepared);
            attachmentProcessor.addAttachment(sqlAttachment,
                    new FreemarkerAttachmentRenderer(templatePath));
        }
    }
}
