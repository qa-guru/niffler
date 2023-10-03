package guru.qa.niffler.jupiter;

import guru.qa.niffler.api.CategoryService;
import guru.qa.niffler.api.SpendService;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.SpendJson;
import okhttp3.OkHttpClient;
import org.junit.jupiter.api.extension.*;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.util.Date;

public class GenerateSpendExtension implements ParameterResolver, BeforeEachCallback {

    public static final ExtensionContext.Namespace NAMESPACE_SPEND = ExtensionContext.Namespace
          .create(GenerateSpendExtension.class);

    public static ExtensionContext.Namespace NAMESPACE_CATEGORY = ExtensionContext.Namespace
        .create(GenerateCategoryExtension.class);

    private static final OkHttpClient httpClient = new OkHttpClient.Builder()
          .build();

    private final Retrofit retrofit = new Retrofit.Builder()
          .client(httpClient)
          .baseUrl("http://127.0.0.1:8093")
          .addConverterFactory(JacksonConverterFactory.create())
          .build();

    private final SpendService spendService = retrofit.create(SpendService.class);

    private final CategoryService categoryService = retrofit.create(CategoryService.class);

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        GenerateSpend annotation = context.getRequiredTestMethod().getAnnotation(GenerateSpend.class);

        // вырезать внизу //
            CategoryJson category = new CategoryJson();
            category.setCategory(annotation.category().category());
            category.setUsername(annotation.category().username());

            CategoryJson createdCategory = categoryService.addCategory(category)
                .execute()
                .body();
            context.getStore(NAMESPACE_CATEGORY).put("category", createdCategory);
        // вырезать вверху //

        if (annotation != null) {
            SpendJson spend = new SpendJson();
            spend.setUsername(annotation.username());
            spend.setDescription(annotation.description());
            spend.setCategory(annotation.category());
            spend.setSpendDate(new Date());
            spend.setCurrency(annotation.currency());
            spend.setAmount(annotation.amount());

            SpendJson created = spendService.addSpend(spend)
                  .execute()
                  .body();
            context.getStore(NAMESPACE_SPEND).put("spend", created);
        }
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType().isAssignableFrom(SpendJson.class);
    }

    @Override
    public SpendJson resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return extensionContext.getStore(NAMESPACE_SPEND).get("spend", SpendJson.class);
    }
}
