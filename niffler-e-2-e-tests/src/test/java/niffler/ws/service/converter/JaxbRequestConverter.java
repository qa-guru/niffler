package niffler.ws.service.converter;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.soap.MessageFactory;
import jakarta.xml.soap.SOAPEnvelope;
import jakarta.xml.soap.SOAPException;
import jakarta.xml.soap.SOAPMessage;
import jakarta.xml.soap.SOAPPart;
import okhttp3.RequestBody;
import org.w3c.dom.Document;
import retrofit2.Converter;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLOutputFactory;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

final class JaxbRequestConverter<T> implements Converter<T, RequestBody> {
    final XMLOutputFactory xmlOutputFactory = XMLOutputFactory.newInstance();
    final JAXBContext context;
    final Class<T> type;

    JaxbRequestConverter(JAXBContext context, Class<T> type) {
        this.context = context;
        this.type = type;
    }

    @Override
    public RequestBody convert(final T value) throws IOException {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
            Marshaller marshaller = context.createMarshaller();
            marshaller.marshal(value, document);

            SOAPMessage soapMessage = MessageFactory.newInstance().createMessage();
            soapMessage.getSOAPBody().addDocument(document);
            SOAPPart part = soapMessage.getSOAPPart();
            SOAPEnvelope envelope = part.getEnvelope();
            envelope.addNamespaceDeclaration("gs", "niffler-userdata");
            soapMessage.writeTo(outputStream);

            return RequestBody.create(JaxbConverterFactory.XML, outputStream.toByteArray());
        } catch (JAXBException | ParserConfigurationException | SOAPException e) {
            throw new RuntimeException(e);
        }
    }
}