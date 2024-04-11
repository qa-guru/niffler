package guru.qa.niffler.jupiter.extension;

import com.fasterxml.jackson.databind.ObjectMapper;
import guru.qa.niffler.jupiter.annotation.GqlReq;
import guru.qa.niffler.model.gql.GqlRequest;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.junit.platform.commons.support.AnnotationSupport;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;

public class GqlReqResolver implements ParameterResolver {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        GqlReq annotation = AnnotationSupport.findAnnotation(parameterContext.getParameter(), GqlReq.class)
                .orElse(null);

        return annotation != null && !"".equals(annotation.value()) &&
                parameterContext.getParameter().getType().isAssignableFrom(GqlRequest.class);
    }

    @Override
    public GqlRequest resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        String path = AnnotationSupport.findAnnotation(parameterContext.getParameter(), GqlReq.class)
                .orElseThrow()
                .value();
        try (InputStream is = new ClassPathResource(path).getInputStream()) {
            return OBJECT_MAPPER.readValue(is, GqlRequest.class);
        } catch (IOException e) {
            throw new ParameterResolutionException("Error while reading resource", e);
        }
    }
}
