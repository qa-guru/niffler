package niffler.test.ws;

import io.qameta.allure.AllureId;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.soap.MessageFactory;
import jakarta.xml.soap.SOAPEnvelope;
import jakarta.xml.soap.SOAPMessage;
import jakarta.xml.soap.SOAPPart;
import niffler.ws.NifflerUserdataWsService;
import niffler.ws.model.wsdl.CurrentUserRequest;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayOutputStream;

public class UserDataWsTest {

    private final NifflerUserdataWsService nus = new NifflerUserdataWsService();

    @Test
    // todo implement!
    @Disabled
    @AllureId("101")
    void updateUserApiTest() throws Exception {
        CurrentUserRequest cur = new CurrentUserRequest();
        cur.setUsername("dima");

        Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        Marshaller marshaller = JAXBContext.newInstance(CurrentUserRequest.class).createMarshaller();
        marshaller.marshal(cur, document);

        SOAPMessage soapMessage = MessageFactory.newInstance().createMessage();
        soapMessage.getSOAPBody().addDocument(document);
        SOAPPart part = soapMessage.getSOAPPart();
        SOAPEnvelope envelope = part.getEnvelope();
        envelope.addNamespaceDeclaration("gs", "niffler-userdata");


        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        soapMessage.writeTo(outputStream);

        System.out.println(new String(outputStream.toByteArray()));

    }
}
