package niffler.jupiter;

import com.fasterxml.jackson.databind.ObjectMapper;
import niffler.model.UserJson;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.params.converter.ArgumentConversionException;
import org.junit.jupiter.params.converter.ArgumentConverter;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class UserDataConverter implements ArgumentConverter {
    private ClassLoader classLoader = this.getClass().getClassLoader();
    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Object convert(Object source, ParameterContext context) throws ArgumentConversionException {
        if (!(source instanceof String)) {
            throw new ArgumentConversionException("Work only for Strings!");
        }
        try (InputStream inputStream = classLoader.getResourceAsStream((String) source);
             InputStreamReader inputStreamReader = new InputStreamReader(inputStream)) {
            return objectMapper.readValue(inputStreamReader, UserJson.class);
        } catch (IOException e) {
            throw new ArgumentConversionException("Failed to convert", e);
        }
    }
}
