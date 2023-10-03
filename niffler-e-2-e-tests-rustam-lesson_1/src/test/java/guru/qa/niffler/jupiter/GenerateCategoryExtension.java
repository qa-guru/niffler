package guru.qa.niffler.jupiter;

import guru.qa.niffler.api.CategoryService;
import guru.qa.niffler.model.CategoryJson;
import okhttp3.*;
import org.junit.jupiter.api.extension.*;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.io.IOException;

public class GenerateCategoryExtension implements ParameterResolver, BeforeEachCallback {

    public static ExtensionContext.Namespace NAMESPACE_CATEGORY = ExtensionContext.Namespace
          .create(GenerateCategoryExtension.class);

    public static final OkHttpClient httpClient = new OkHttpClient.Builder()
          .build();

    private final Retrofit retrofit = new Retrofit.Builder()
          .client(httpClient)
          .baseUrl("http://127.0.0.1:8093")
          .addConverterFactory(JacksonConverterFactory.create())
          .build();

    private final CategoryService categoryService = retrofit.create(CategoryService.class);

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        GenerateCategory annotation = context.getRequiredTestMethod().getAnnotation(GenerateCategory.class);
        if (annotation != null) {
            CategoryJson category = new CategoryJson();
            category.setCategory(annotation.category());
            category.setUsername(annotation.username());

            CategoryJson created = categoryService.addCategory(category)
                  .execute()
                  .body();
            context.getStore(NAMESPACE_CATEGORY).put("category", created);
        }
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType().isAssignableFrom(CategoryJson.class);
    }

    @Override
    public CategoryJson resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return extensionContext.getStore(NAMESPACE_CATEGORY).get("category", CategoryJson.class);
    }

}
