package niffler.jupiter;

import niffler.api.SpendService;
import niffler.model.SpendModel;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;

import java.util.Date;
import java.util.List;
import java.util.Arrays;
import java.util.ArrayList;
import java.lang.reflect.Field;

public class SpendExtension implements BeforeEachCallback, AfterEachCallback {
    public static final ExtensionContext.Namespace NAMESPACE
            = ExtensionContext.Namespace.create(SpendExtension.class);

    @Override
    public void beforeEach(ExtensionContext context) throws IllegalAccessException {
        Object testInstance = context.getRequiredTestInstance();
        List<Field> fields = Arrays.stream(testInstance.getClass().getDeclaredFields())
                .filter(f -> f.getType().isAssignableFrom(SpendModel.class))
                .filter(f -> f.isAnnotationPresent(Spend.class)).toList();
        List<SpendModel> spends = new ArrayList<>();

        for (Field field : fields) {
            Spend spendFields = field.getAnnotation(Spend.class);
            SpendModel spend = SpendModel.builder()
                    .spendDate(new Date())
                    .category(spendFields.category())
                    .currency(spendFields.currency())
                    .amount(spendFields.amount())
                    .description(spendFields.description())
                    .username(spendFields.username())
                    .build();

            spend = SpendService.addSpend(spend);
            field.setAccessible(true);
            field.set(testInstance, spend);
            spends.add(spend);
        }
        context.getStore(NAMESPACE).put(getTestId(context), spends);
    }

    @Override
    public void afterEach(ExtensionContext context){
        List<SpendModel> spends = context.getStore(NAMESPACE).get(getTestId(context), List.class);
        for (SpendModel spend : spends) {
            SpendService.deleteSpends(spend);
        }
    }

    private String getTestId(ExtensionContext context) {
        return context.getRequiredTestClass().getName() + ": " +
                context.getRequiredTestMethod().getName();
    }
}
