package guru.qa.niffler.test.web.other;

import guru.qa.niffler.db.logging.JsonAttachment;
import guru.qa.niffler.model.Spend7Json;
import guru.qa.niffler.test.web.BaseWebTest;
import io.qameta.allure.Allure;
import io.qameta.allure.Attachment;
import io.qameta.allure.attachment.AttachmentData;
import io.qameta.allure.attachment.AttachmentProcessor;
import io.qameta.allure.attachment.AttachmentRenderer;
import io.qameta.allure.attachment.DefaultAttachmentProcessor;
import io.qameta.allure.attachment.FreemarkerAttachmentRenderer;
import java.io.FileReader;
import java.io.IOException;
import org.junit.jupiter.api.Test;
import wiremock.net.minidev.json.JSONObject;
import wiremock.net.minidev.json.parser.JSONParser;
import wiremock.net.minidev.json.parser.ParseException;


public class LessonTestRustam extends BaseWebTest {

//  @BeforeEach
//  void setup(String param) {
//    System.out.println(param);
//  }

  @Test
  @MockAnnotation
  void loginTest(){
    Allure.addAttachment("xml example", getClass().getClassLoader().getResourceAsStream(
        "rest/spend0.json"
    ));
  }

  private final AttachmentProcessor<AttachmentData> attachmentProcessor = new DefaultAttachmentProcessor();
  private final AttachmentRenderer<AttachmentData> attachmentRenderer = new FreemarkerAttachmentRenderer("files.ftl");
//
//  @Test
//  void attachTest(){
//    attachmentProcessor.addAttachment((AttachmentData) getClass().getClassLoader().getResourceAsStream(
//        "rest/spend0.json"), attachmentRenderer);
//  }

  @Test
  void attachTest() throws IOException {
    Allure.getLifecycle().addAttachment("JSON example", "text/json", "json",
        getClass().getClassLoader().getResourceAsStream("rest/spend0.json"));

    Allure.getLifecycle().addAttachment("XML example", "text/xml", "xml",
        getClass().getClassLoader().getResourceAsStream("META-INF/persistence.xml"));

    attachJson();
  }

  @Attachment(value = "Тестовый JSON", type = "text/json", fileExtension = "json")
  public void attachJson() throws IOException {
//    InputStream resourceAsStream = getClass().getClassLoader()
//        .getResourceAsStream("rest/spend0.json");

//    byte[] bytes = FileUtils.readFileToByteArray(new File("niffler-e-2-e-tests-rustam-lesson_7/src/test/resources/rest/spend0.json"));

    JSONParser jsonParser = new JSONParser();
    JsonAttachment jsonAttachment = null;
    try(FileReader reader = new FileReader("C:\\ProjectsJob\\petNew\\niffler\\niffler-e-2-e-tests-rustam-lesson_7\\src\\test\\resources\\rest\\spend0.json")) {
      JSONObject object = (JSONObject) jsonParser.parse(reader);
      jsonAttachment = new JsonAttachment(
          "JSON",
          new Spend7Json(
          object.getAsString("spendDate"),
          object.getAsString("category"),
          object.getAsString("currency"),
          object.getAsString("amount"),
          object.getAsString("description"),
          object.getAsString("username")
          )
      );
    } catch (ParseException e) {
      throw new RuntimeException(e);
    }

    attachmentProcessor.addAttachment(
        jsonAttachment,
        attachmentRenderer
    );
  }
}
