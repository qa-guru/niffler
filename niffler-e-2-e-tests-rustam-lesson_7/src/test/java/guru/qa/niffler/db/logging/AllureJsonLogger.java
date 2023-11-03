package guru.qa.niffler.db.logging;

import static org.apache.commons.lang3.StringUtils.isNotEmpty;

import com.jayway.jsonpath.internal.JsonFormatter;
import com.p6spy.engine.spy.appender.StdoutLogger;
import io.qameta.allure.attachment.AttachmentData;
import io.qameta.allure.attachment.AttachmentProcessor;
import io.qameta.allure.attachment.AttachmentRenderer;
import io.qameta.allure.attachment.DefaultAttachmentProcessor;
import io.qameta.allure.attachment.FreemarkerAttachmentRenderer;

public class AllureJsonLogger extends StdoutLogger {

  private final AttachmentProcessor<AttachmentData>
      attachmentProcessor = new DefaultAttachmentProcessor();
  private final AttachmentRenderer<AttachmentData>
      attachmentRenderer = new FreemarkerAttachmentRenderer("files.ftl");

//  @Override
//  public void logText(String text) {
//    super.logText(text);
//    if (isNotEmpty(text)) {
//      JsonAttachment jsonAttachment = new JsonAttachment(
//          "JSON",
//          JsonFormatter.prettyPrint(text)
//      );
//      attachmentProcessor.addAttachment(jsonAttachment, attachmentRenderer);
//    }
//  }
}
