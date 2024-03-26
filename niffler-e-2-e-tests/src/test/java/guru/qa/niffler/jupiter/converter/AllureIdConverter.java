package guru.qa.niffler.jupiter.converter;

import io.qameta.allure.Allure;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.params.converter.ArgumentConversionException;
import org.junit.jupiter.params.converter.ArgumentConverter;

public class AllureIdConverter implements ArgumentConverter {
  @Override
  public Object convert(Object o, ParameterContext parameterContext) throws ArgumentConversionException {
    if (o instanceof String allureId) {
      Allure.label("AS_ID", allureId);
    }
    return o;
  }
}
