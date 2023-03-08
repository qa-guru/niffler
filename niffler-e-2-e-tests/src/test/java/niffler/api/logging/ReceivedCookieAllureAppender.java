package niffler.api.logging;

import io.qameta.allure.attachment.AttachmentData;
import io.qameta.allure.attachment.AttachmentProcessor;
import io.qameta.allure.attachment.DefaultAttachmentProcessor;
import io.qameta.allure.attachment.FreemarkerAttachmentRenderer;

public class ReceivedCookieAllureAppender {
    private final AttachmentProcessor<AttachmentData> processor = new DefaultAttachmentProcessor();
    private final String receivedCookieTemplatePath = "received-cookie.ftl";

    public void logCookie(ReceivedCookieAttachment attachment) {
        processor.addAttachment(attachment, new FreemarkerAttachmentRenderer(receivedCookieTemplatePath));
    }
}
