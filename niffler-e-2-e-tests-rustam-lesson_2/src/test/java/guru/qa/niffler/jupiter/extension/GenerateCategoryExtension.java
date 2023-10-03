package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.api.CategoryService;
import guru.qa.niffler.jupiter.annotation.GenerateCategory;
import guru.qa.niffler.model.CategoryJson;
import okhttp3.*;
import org.junit.jupiter.api.extension.*;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class GenerateCategoryExtension implements ParameterResolver, BeforeEachCallback {

    public static ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace
          .create(GenerateCategoryExtension.class);

    public static final OkHttpClient httpClient = new OkHttpClient.Builder()
          .build();

//    public static final OkHttpClient httpClient = new OkHttpClient().newBuilder().addInterceptor(new Interceptor() {
//        @Override
//        public okhttp3.Response intercept(Chain chain) throws IOException {
//            Request originalRequest = chain.request();
//
//            Request.Builder builder = originalRequest.newBuilder()
//                  .header("Content-Type", "application/text; charset=utf-8");
//
//            Request newRequest = builder.build();
//            return chain.proceed(newRequest);
//        }
//    }).build();

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
//            CategoryJson created = categoryService.addCategory("application/json; charset=UTF-8", category)
                  .execute()
                  .body();
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
