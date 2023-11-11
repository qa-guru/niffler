package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.api.client.CategoryRestClient;
import guru.qa.niffler.api.client.Spend7RestClient;
import guru.qa.niffler.api.service.CategoryService;
import guru.qa.niffler.api.service.Spend7Service;
import guru.qa.niffler.jupiter.annotation.GenerateSpend;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.Spend7Json;
import java.util.Date;
import okhttp3.OkHttpClient;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class Generate7SpendExtension implements ParameterResolver, BeforeEachCallback {

    public static ExtensionContext.Namespace NAMESPACE_7 = ExtensionContext.Namespace
          .create(Generate7SpendExtension.class);

    private final Spend7RestClient spend7RestClient = new Spend7RestClient();

    private final CategoryRestClient categoryRestClient = new CategoryRestClient();

//    private static final OkHttpClient httpClient = new OkHttpClient.Builder()
//        .build();
//
//    private final Retrofit retrofit = new Retrofit.Builder()
//        .client(httpClient)
//        .baseUrl("http://127.0.0.1:8093")
//        .addConverterFactory(JacksonConverterFactory.create())
//        .build();
//
//    private final Spend7Service spend7Service = retrofit.create(Spend7Service.class);

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        GenerateSpend annotation = context.getRequiredTestMethod().getAnnotation(GenerateSpend.class);
        if (annotation != null) {
            Spend7Json spend = new Spend7Json();
            spend.setUsername(annotation.username());
            spend.setDescription(annotation.description());
            if (annotation.randomCategory().handleAnnotation()) {
//                CategoryJson randomCategory = new CategoryJson();
//                randomCategory.setCategory(annotation.randomCategory().category());
//                randomCategory.setUsername(annotation.randomCategory().username());
//                spend.setCategory(randomCategory);
                CategoryJson category = new CategoryJson();
                category.setCategory(annotation.randomCategory().category());
                category.setUsername(annotation.randomCategory().username());

                categoryRestClient.addCategory(category);

                spend.setCategory(category.getCategory());
            } else {
                spend.setCategory(annotation.category());
            }
            spend.setSpendDate(new Date());
            spend.setCurrency(annotation.currency());
            spend.setAmount(annotation.amount());

            Spend7Json created = spend7RestClient.addSpend(spend);
//            Spend7Json created = spend7Service.addSpend(spend).execute().body();
            context.getStore(NAMESPACE_7).put("spend", created);
        }
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType().isAssignableFrom(Spend7Json.class);
    }

    @Override
    public Spend7Json resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return extensionContext.getStore(NAMESPACE_7).get("spend", Spend7Json.class);
    }
}
