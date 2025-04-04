package guru.qa.niffler.api.converter;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.soap.MessageFactory;
import jakarta.xml.soap.SOAPEnvelope;
import jakarta.xml.soap.SOAPException;
import jakarta.xml.soap.SOAPMessage;
import okhttp3.RequestBody;
import org.w3c.dom.Document;
import retrofit2.Converter;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@ParametersAreNonnullByDefault
final class JaxbRequestConverter<T> implements Converter<T, RequestBody> {

  public static final String NAMESPACE_PREFIX = "tns";
  private final @Nonnull String messageNamespace;
  private final @Nonnull JAXBContext context;

  JaxbRequestConverter(String messageNamespace, JAXBContext context) {
    this.messageNamespace = messageNamespace;
    this.context = context;
  }

  @Override
  public @Nonnull RequestBody convert(final T value) throws IOException {
    try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
      final SOAPMessage soapMessage = MessageFactory.newInstance().createMessage();
      final Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();

      final Marshaller marshaller = context.createMarshaller();
      marshaller.marshal(value, document);

      soapMessage.getSOAPBody().addDocument(document);
      final SOAPEnvelope envelope = soapMessage.getSOAPPart().getEnvelope();
      envelope.addNamespaceDeclaration(NAMESPACE_PREFIX, messageNamespace);
      soapMessage.writeTo(outputStream);

      return RequestBody.create(JaxbConverterFactory.XML, outputStream.toByteArray());
    } catch (JAXBException | ParserConfigurationException | SOAPException e) {
      throw new RuntimeException(e);
    }
  }
}