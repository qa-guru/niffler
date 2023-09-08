package com.github.dtitar.jupiter.extension;

import com.github.dtitar.api.SpendServiceClient;
import com.github.dtitar.jupiter.annotation.Category;
import com.github.dtitar.model.rest.CategoryJson;
import com.github.dtitar.model.rest.SpendJson;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;

import java.io.IOException;

public class CategoryExtension implements BeforeEachCallback, ParameterResolver {

    public static ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(CategoryExtension.class);
    private SpendServiceClient spendService = new SpendServiceClient();

    @Override
    public void beforeEach(ExtensionContext context) throws IOException {
        Category annotation = context.getRequiredTestMethod()
                .getAnnotation(Category.class);
        if (annotation != null) {
            CategoryJson category = new CategoryJson();
            category.setUsername(annotation.username());
            category.setCategory(annotation.description());
            spendService.addCategory(category);
            context.getStore(NAMESPACE)
                    .put("category", category);
        }
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter()
                .getType()
                .isAssignableFrom(CategoryJson.class);
    }

    @Override
    public CategoryJson resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return extensionContext
                .getStore(CategoryExtension.NAMESPACE)
                .get("category", CategoryJson.class);
    }
}
