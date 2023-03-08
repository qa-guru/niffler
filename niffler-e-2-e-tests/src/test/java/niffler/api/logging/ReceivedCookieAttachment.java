package niffler.api.logging;

import io.qameta.allure.attachment.AttachmentData;

public class ReceivedCookieAttachment implements AttachmentData {
    private final String name;
    private final String value;

    public ReceivedCookieAttachment(String name, String value) {
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