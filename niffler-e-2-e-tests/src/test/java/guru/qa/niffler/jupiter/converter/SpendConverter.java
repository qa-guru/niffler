package guru.qa.niffler.jupiter.converter;

import com.fasterxml.jackson.databind.ObjectMapper;
import guru.qa.niffler.model.rest.SpendJson;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.params.converter.ArgumentConversionException;
import org.junit.jupiter.params.converter.ArgumentConverter;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;

public class SpendConverter implements ArgumentConverter {

  private static final ObjectMapper om = new ObjectMapper();

  @Override
  public SpendJson convert(Object source, ParameterContext context) throws ArgumentConversionException {
    if (source instanceof String path) {
      try (InputStream is = new ClassPathResource(path).getInputStream()) {
        return om.readValue(is, SpendJson.class);
      } catch (IOException e) {
        throw new ArgumentConversionException("Failed to convert", e);
      }
    } else throw new ArgumentConversionException("Only String type supported");
  }
}
