package guru.qa.niffler.api.logging;

import io.qameta.allure.attachment.*;

public final class ReceivedCookieRespAllureAppender {

  private final static String RECEIVED_COOKIE_RESP_TEMPLATE_PATH = "received-cookie-resp.ftl";

  private final AttachmentProcessor<AttachmentData> processor = new DefaultAttachmentProcessor();

  public void addAttachment(ReceivedCookieRespAttachment attachment) {
    processor.addAttachment(attachment, new FreemarkerAttachmentRenderer(RECEIVED_COOKIE_RESP_TEMPLATE_PATH));
  }

}
