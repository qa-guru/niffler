package guru.qa.niffler.data.logging;

import io.qameta.allure.attachment.AttachmentData;

public class SqlRequestAttachment implements AttachmentData {
    private final String name;
    private final String sql;

    public SqlRequestAttachment(String name, String sql) {
        this.name = name;
        this.sql = sql;
    }

    public String getSql() {
        return sql;
    }

    @Override
    public String getName() {
        return name;
    }
}