package niffler.jupiter.extension;

import io.qameta.allure.AllureId;
import niffler.api.NifflerAuthClient;
import niffler.api.NifflerSpendClient;
import niffler.api.NifflerUserdataClient;
import niffler.config.Config;
import niffler.jupiter.annotation.ApiLogin;
import niffler.jupiter.annotation.GenerateCategory;
import niffler.jupiter.annotation.GenerateSpend;
import niffler.jupiter.annotation.GenerateUser;
import niffler.jupiter.annotation.User;
import niffler.model.CategoryJson;
import niffler.model.SpendJson;
import niffler.model.UserJson;
import niffler.utils.DateUtils;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import retrofit2.Response;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static niffler.utils.DataUtils.generateRandomPassword;
import static niffler.utils.DataUtils.generateRandomUsername;

public class CreateUserExtension implements BeforeEachCallback, ParameterResolver {

    private final NifflerAuthClient authClient = new NifflerAuthClient();
    private final NifflerUserdataClient userdataClient = new NifflerUserdataClient();
    private final NifflerSpendClient spendClient = new NifflerSpendClient();
    protected static final Config CFG = Config.getConfig();

    public static final ExtensionContext.Namespace
            ON_METHOD_USERS_NAMESPACE = ExtensionContext.Namespace.create(CreateUserExtension.class, Selector.METHOD),
            API_LOGIN_USERS_NAMESPACE = ExtensionContext.Namespace.create(CreateUserExtension.class, Selector.NESTED);

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        final String testId = getTestId(context);
        Map<Selector, GenerateUser> userAnnotations = extractGenerateUserAnnotations(context);
        for (Map.Entry<Selector, GenerateUser> entry : userAnnotations.entrySet()) {
            String username = entry.getValue().username();
            String password = entry.getValue().password();
            if ("".equals(username)) {
                username = generateRandomUsername();
            }
            if ("".equals(password)) {
                password = generateRandomPassword();
            }
            UserJson userJson = apiRegister(username, password);
            GenerateCategory[] categories = entry.getValue().categories();
            List<CategoryJson> createdCategories = new ArrayList<>();
            if (categories != null && categories.length > 0) {
                for (GenerateCategory category : categories) {
                    CategoryJson cj = new CategoryJson();
                    cj.setUsername(username);
                    cj.setCategory(category.value());
                    createdCategories.add(spendClient.createCategory(cj));
                }
            }

            GenerateSpend[] spends = entry.getValue().spends();
            List<SpendJson> createdSpends = new ArrayList<>();
            if (spends != null && spends.length > 0) {
                for (GenerateSpend spend : spends) {
                    SpendJson sj = new SpendJson();
                    sj.setUsername(username);
                    sj.setCategory(spend.spendCategory());
                    sj.setAmount(spend.amount());
                    sj.setCurrency(spend.currency());
                    sj.setDescription(spend.spendName());
                    sj.setSpendDate(DateUtils.addDaysToDate(new Date(), Calendar.DAY_OF_WEEK, spend.addDaysToSpendDate()));
                    createdSpends.add(spendClient.createSpend(sj));
                }
            }

            userJson.setCategoryJsons(createdCategories);
            userJson.setSpendJsons(createdSpends);
            context.getStore(entry.getKey().getNamespace()).put(testId, userJson);
        }
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType().isAssignableFrom(UserJson.class)
                && parameterContext.getParameter().isAnnotationPresent(User.class);
    }

    @Override
    public UserJson resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        final String testId = getTestId(extensionContext);
        User annotation = parameterContext.getParameter().getAnnotation(User.class);
        return extensionContext.getStore(annotation.selector().getNamespace()).get(testId, UserJson.class);
    }

    private String getTestId(ExtensionContext context) {
        return Objects.requireNonNull(
                context.getRequiredTestMethod().getAnnotation(AllureId.class)
        ).value();
    }

    public enum Selector {
        METHOD, NESTED;

        public ExtensionContext.Namespace getNamespace() {
            switch (this) {
                case METHOD -> {
                    return ON_METHOD_USERS_NAMESPACE;
                }
                case NESTED -> {
                    return API_LOGIN_USERS_NAMESPACE;
                }
                default -> {
                    throw new IllegalStateException();
                }
            }
        }
    }

    private Map<Selector, GenerateUser> extractGenerateUserAnnotations(ExtensionContext context) {
        Map<Selector, GenerateUser> annotationsOnTest = new HashMap<>();
        GenerateUser annotationOnMethod = context.getRequiredTestMethod().getAnnotation(GenerateUser.class);
        if (annotationOnMethod != null && annotationOnMethod.handleAnnotation()) {
            annotationsOnTest.put(Selector.METHOD, annotationOnMethod);
        }
        ApiLogin apiLoginAnnotation = context.getRequiredTestMethod().getAnnotation(ApiLogin.class);
        if (apiLoginAnnotation != null && apiLoginAnnotation.nifflerUser().handleAnnotation()) {
            annotationsOnTest.put(Selector.NESTED, apiLoginAnnotation.nifflerUser());
        }
        return annotationsOnTest;
    }

    private UserJson apiRegister(String username, String password) throws Exception {
        authClient.authorize();
        Response<Void> res = authClient.register(username, password);
        if (res.code() != 201) {
            throw new RuntimeException("User is not registered");
        }
        UserJson currentUser = userdataClient.getCurrentUser(username);
        currentUser.setPassword(password);
        return currentUser;
    }
}
