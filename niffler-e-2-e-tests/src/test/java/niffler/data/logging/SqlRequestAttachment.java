package niffler.data.logging;

import io.qameta.allure.attachment.AttachmentData;

public class SqlRequestAttachment implements AttachmentData {
    private final String name;
    private final String preparedStatement;
    private final String sql;

    public SqlRequestAttachment(String name, String preparedStatement, String sql) {
        this.name = name;
        this.preparedStatement = preparedStatement;
        this.sql = sql;
    }

    public String getPreparedStatement() {
        return preparedStatement;
    }

    public String getSql() {
        return sql;
    }

    @Override
    public String getName() {
        return name;
    }
}