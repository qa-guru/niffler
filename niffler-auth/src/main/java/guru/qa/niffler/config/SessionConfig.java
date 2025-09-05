package guru.qa.niffler.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import guru.qa.niffler.service.serialization.JdbcSessionObjectMapperConfigurer;
import jakarta.annotation.Nonnull;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.support.GenericConversionService;
import org.springframework.core.serializer.Deserializer;
import org.springframework.core.serializer.Serializer;
import org.springframework.core.serializer.support.DeserializingConverter;
import org.springframework.core.serializer.support.SerializingConverter;
import org.springframework.session.config.SessionRepositoryCustomizer;
import org.springframework.session.jdbc.JdbcIndexedSessionRepository;
import org.springframework.session.jdbc.config.annotation.web.http.EnableJdbcHttpSession;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Webauth (passkeys) instead Oauth should use JSON-serialization
 * Also, very important to use the same ObjectMapper that was configured via
 * @see JdbcSessionObjectMapperConfigurer#apply(ClassLoader, ObjectMapper)
 * in this config and jdbcOAuth2AuthorizationService bean
 * @link <a href="https://docs.spring.io/spring-session/reference/configuration/jdbc.html#session-attributes-as-json">doc</a>
 */
@Configuration
@EnableJdbcHttpSession(maxInactiveIntervalInSeconds = 3600)
public class SessionConfig implements BeanClassLoaderAware {

  private static final String CREATE_SESSION_ATTRIBUTE_QUERY = """
      INSERT INTO %TABLE_NAME%_ATTRIBUTES (SESSION_PRIMARY_ID, ATTRIBUTE_NAME, ATTRIBUTE_BYTES)
      VALUES (?, ?, convert_from(?, 'UTF8')::jsonb)
      """;

  private static final String UPDATE_SESSION_ATTRIBUTE_QUERY = """
      UPDATE %TABLE_NAME%_ATTRIBUTES
      SET ATTRIBUTE_BYTES = convert_from(?, 'UTF8')::jsonb
      WHERE SESSION_PRIMARY_ID = ?
      AND ATTRIBUTE_NAME = ?
      """;

  /**
   * For ObjectMapper configuration
   * @see guru.qa.niffler.config.SessionConfig#springSessionConversionService(ObjectMapper)
   */
  private ClassLoader classLoader;

  @Bean
  public SessionRepositoryCustomizer<JdbcIndexedSessionRepository> customizer() {
    return (sessionRepository) -> {
      sessionRepository.setCreateSessionAttributeQuery(CREATE_SESSION_ATTRIBUTE_QUERY);
      sessionRepository.setUpdateSessionAttributeQuery(UPDATE_SESSION_ATTRIBUTE_QUERY);
    };
  }

  /**
   * Do not use @Primary ObjectMapper directly!
   * Only configured via JdbcSessionObjectMapperConfigurer
   */
  @Bean("springSessionConversionService")
  public GenericConversionService springSessionConversionService(ObjectMapper objectMapper) {
    ObjectMapper configuredOm = JdbcSessionObjectMapperConfigurer.apply(this.classLoader, objectMapper);
    final GenericConversionService converter = new GenericConversionService();
    converter.addConverter(Object.class, byte[].class, new SerializingConverter(new JsonSerializer(configuredOm)));
    converter.addConverter(byte[].class, Object.class, new DeserializingConverter(new JsonDeserializer(configuredOm)));
    return converter;
  }

  @Override
  public void setBeanClassLoader(@Nonnull ClassLoader classLoader) {
    this.classLoader = classLoader;
  }

  static class JsonSerializer implements Serializer<Object> {
    private final ObjectMapper objectMapper;

    JsonSerializer(ObjectMapper objectMapper) {
      this.objectMapper = objectMapper;
    }

    @Override
    public void serialize(@Nonnull Object object, @Nonnull OutputStream outputStream) throws IOException {
      this.objectMapper.writeValue(outputStream, object);
    }
  }

  static class JsonDeserializer implements Deserializer<Object> {
    private final ObjectMapper objectMapper;

    JsonDeserializer(ObjectMapper objectMapper) {
      this.objectMapper = objectMapper;
    }

    @Override
    @Nonnull
    public Object deserialize(@Nonnull InputStream inputStream) throws IOException {
      return this.objectMapper.readValue(inputStream, Object.class);
    }
  }
}
