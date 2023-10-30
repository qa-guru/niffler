package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.api.client.Spend7RestClient;
import guru.qa.niffler.jupiter.annotation.GenerateSpend;
import guru.qa.niffler.model.Spend7Json;
import java.util.Date;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;

public class Generate7SpendExtension implements ParameterResolver, BeforeEachCallback {

    public static ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace
          .create(Generate7SpendExtension.class);

//    private final Spend7RestClient spend7RestClient = new Spend7RestClient();

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        GenerateSpend annotation = context.getRequiredTestMethod().getAnnotation(GenerateSpend.class);
        if (annotation != null) {
            Spend7Json spend = new Spend7Json();
            spend.setUsername(annotation.username());
            spend.setDescription(annotation.description());
            spend.setCategory(annotation.category());
            spend.setSpendDate(new Date());
            spend.setCurrency(annotation.currency());
            spend.setAmount(annotation.amount());
            Spend7RestClient spend7RestClient = new Spend7RestClient();
            Spend7Json created = spend7RestClient.addSpend(spend);
            context.getStore(NAMESPACE).put("spend", created);
        }
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType().isAssignableFrom(Spend7Json.class);
    }

    @Override
    public Spend7Json resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return extensionContext.getStore(NAMESPACE).get("spend", Spend7Json.class);
    }
}
