package guru.qa.niffler.api.converter;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.annotation.XmlRootElement;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Objects;

public final class JaxbConverterFactory extends Converter.Factory {

    static final MediaType XML = MediaType.get("application/xml; charset=utf-8");

    public static @Nonnull JaxbConverterFactory create(@Nonnull String messageNamespace, @Nonnull JAXBContext context) {
        return new JaxbConverterFactory(messageNamespace, Objects.requireNonNull(context));
    }

    public static @Nonnull JaxbConverterFactory create(@Nonnull String messageNamespace) {
        return new JaxbConverterFactory(messageNamespace, null);
    }

    private final @Nonnull String messageNamespace;
    private final @Nullable JAXBContext context;

    private JaxbConverterFactory(@Nonnull String messageNamespace, @Nullable JAXBContext context) {
        this.messageNamespace = messageNamespace;
        this.context = context;
    }

    @Override
    public @Nullable Converter<?, RequestBody> requestBodyConverter(
            @Nonnull Type type,
            @Nonnull Annotation[] parameterAnnotations,
            @Nonnull Annotation[] methodAnnotations,
            @Nonnull Retrofit retrofit) {
        if (type instanceof Class<?> cls && cls.isAnnotationPresent(XmlRootElement.class)) {
            return new JaxbRequestConverter<>(messageNamespace, contextForType(cls));
        }
        return null;
    }

    @Override
    public @Nullable Converter<ResponseBody, ?> responseBodyConverter(
            @Nonnull Type type,
            @Nonnull Annotation[] annotations,
            @Nonnull Retrofit retrofit) {
        if (type instanceof Class<?> cls && cls.isAnnotationPresent(XmlRootElement.class)) {
            return new JaxbResponseConverter<>(contextForType(cls), cls);
        }
        return null;
    }

    private @Nonnull JAXBContext contextForType(@Nonnull Class<?> type) {
        try {
            return context != null
                    ? context
                    : JAXBContext.newInstance(type);
        } catch (JAXBException e) {
            throw new IllegalArgumentException(e);
        }
    }
}
