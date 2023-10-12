package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.api.client.CategoryRestClient;
import guru.qa.niffler.jupiter.annotation.GenerateCategory;
import guru.qa.niffler.model.CategoryJson;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;

public class GenerateCategoryExtension implements ParameterResolver, BeforeEachCallback {

    public static ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace
          .create(GenerateCategoryExtension.class);

    private final CategoryRestClient categoryRestClient = new CategoryRestClient();

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        GenerateCategory annotation = context.getRequiredTestMethod().getAnnotation(GenerateCategory.class);
        if (annotation != null) {
            CategoryJson category = new CategoryJson();
            category.setCategory(annotation.category());
            category.setUsername(annotation.username());

            CategoryJson created = categoryRestClient.addCategory(category);
            context.getStore(NAMESPACE).put("category", created);
        }
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType().isAssignableFrom(CategoryJson.class);
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return extensionContext.getStore(NAMESPACE).get("category", CategoryJson.class);
    }

}
