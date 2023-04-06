package niffler.jupiter.converter;

import com.fasterxml.jackson.databind.ObjectMapper;
import niffler.model.rest.SpendJson;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.params.converter.ArgumentConversionException;
import org.junit.jupiter.params.converter.ArgumentConverter;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Objects;

public class SpendConverter implements ArgumentConverter {

    private ClassLoader cl = this.getClass().getClassLoader();
    private ObjectMapper om = new ObjectMapper();

    @Override
    public SpendJson convert(Object source, ParameterContext context) throws ArgumentConversionException {
        if (!(source instanceof String)) {
            throw new ArgumentConversionException("work only for Strings!");
        }
        try (InputStream is = cl.getResourceAsStream((String) source);
             InputStreamReader isr = new InputStreamReader(Objects.requireNonNull(is))) {
            return om.readValue(isr, SpendJson.class);
        } catch (IOException e) {
            throw new ArgumentConversionException("Failed to convert", e);
        }
    }
}
