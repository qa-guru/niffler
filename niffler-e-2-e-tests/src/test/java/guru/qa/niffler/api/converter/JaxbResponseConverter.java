package guru.qa.niffler.api.converter;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;
import jakarta.xml.soap.MessageFactory;
import jakarta.xml.soap.MimeHeaders;
import jakarta.xml.soap.SOAPException;
import jakarta.xml.soap.SOAPMessage;
import okhttp3.ResponseBody;
import org.w3c.dom.Document;
import retrofit2.Converter;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

final class JaxbResponseConverter<T> implements Converter<ResponseBody, T> {

  private final JAXBContext context;
  private final Class<T> type;

  JaxbResponseConverter(JAXBContext context, Class<T> type) {
    this.context = context;
    this.type = type;
  }

  @Override
  public @Nonnull T convert(@Nonnull final ResponseBody value) throws IOException {
    try (value; InputStream is = value.byteStream()) {
      final MimeHeaders headers = new MimeHeaders();
      Optional.ofNullable(value.contentType())
          .ifPresent(mt -> headers.addHeader("Content-Type", mt.toString()));

      final SOAPMessage response = MessageFactory.newInstance().createMessage(
          headers,
          is
      );
      final Document responseDoc = response.getSOAPBody().extractContentAsDocument();
      final Unmarshaller unmarshaller = context.createUnmarshaller();
      return unmarshaller.unmarshal(responseDoc, type).getValue();
    } catch (JAXBException | SOAPException e) {
      throw new RuntimeException(e);
    }
  }
}