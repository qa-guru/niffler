package niffler.jupiter.extension;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import niffler.jupiter.annotation.GqlReq;
import niffler.test.gql.GraphQlFriendsTest;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;

import java.io.IOException;
import java.io.InputStream;

public class GqlReqResolver implements ParameterResolver {

    private static final ObjectMapper om = new ObjectMapper();
    private final ClassLoader cl = GraphQlFriendsTest.class.getClassLoader();

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().isAnnotationPresent(GqlReq.class) &&
                parameterContext.getParameter().getType().isAssignableFrom(JsonNode.class);
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        String path = parameterContext.getParameter().getAnnotation(GqlReq.class).value();
        try (InputStream is = cl.getResourceAsStream(path)) {
            return om.readValue(is, JsonNode.class);
        } catch (IOException e) {
            throw new ParameterResolutionException("Error while reading resource", e);
        }
    }
}
