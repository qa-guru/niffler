package niffler.jupiter.user;

import lombok.SneakyThrows;
import niffler.api.spend.dto.UserDto;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.params.converter.ArgumentConversionException;
import org.junit.jupiter.params.converter.ArgumentConverter;

import java.io.InputStream;

public class JsonToUserConverter implements ArgumentConverter {

    private final ClassLoader classLoader = getClass().getClassLoader();

    @Override
    @SneakyThrows
    public UserDto convert(Object source, ParameterContext context)
            throws ArgumentConversionException {
        if (!(source instanceof String))
            throw new ArgumentConversionException("Work only for Strings!");

        try (InputStream is = classLoader.getResourceAsStream((String) source)) {
            assert is != null;
            return UserDto.fromJson(IOUtils.toString(is));
        }
    }

}
