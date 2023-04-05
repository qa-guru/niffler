package niffler.ws.service.converter;

import com.google.common.base.Charsets;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;
import jakarta.xml.soap.MessageFactory;
import jakarta.xml.soap.SOAPException;
import jakarta.xml.soap.SOAPMessage;
import okhttp3.ResponseBody;
import org.apache.commons.io.input.ReaderInputStream;
import org.w3c.dom.Document;
import retrofit2.Converter;

import javax.xml.stream.XMLInputFactory;
import java.io.IOException;
import java.io.Reader;

final class JaxbResponseConverter<T> implements Converter<ResponseBody, T> {
    final XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
    final JAXBContext context;
    final Class<T> type;

    JaxbResponseConverter(JAXBContext context, Class<T> type) {
        this.context = context;
        this.type = type;
        xmlInputFactory.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, false);
        xmlInputFactory.setProperty(XMLInputFactory.SUPPORT_DTD, false);
    }

    @Override
    public T convert(ResponseBody value) throws IOException {
        try (value; Reader reader = value.charStream()) {
            SOAPMessage response = MessageFactory.newInstance().createMessage(null, new ReaderInputStream(reader, Charsets.UTF_8));
            Document responseDoc = response.getSOAPBody().extractContentAsDocument();
            Unmarshaller unmarshaller = context.createUnmarshaller();
            return unmarshaller.unmarshal(responseDoc, type).getValue();
        } catch (JAXBException | SOAPException e) {
            throw new RuntimeException(e);
        }
    }
}