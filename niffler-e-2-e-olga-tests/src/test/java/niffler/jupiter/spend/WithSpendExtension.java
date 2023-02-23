package niffler.jupiter.spend;

import niffler.api.spend.SpendApi;
import niffler.api.spend.dto.SpendDto;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

final class WithSpendExtension implements BeforeEachCallback, AfterEachCallback {

    public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(WithSpendExtension.class);

    @Override
    public void beforeEach(ExtensionContext extensionContext) throws Exception {
        Object testInstance = extensionContext.getRequiredTestInstance();
        List<Field> fields = Arrays.stream(testInstance.getClass().getDeclaredFields())
                .filter(f -> f.getType().isAssignableFrom(SpendDto.class))
                .filter(f -> f.isAnnotationPresent(WithSpend.class))
                .toList();

        List<SpendDto> spends = new ArrayList<>();
        for (Field field : fields) {
            WithSpend annotation = field.getAnnotation(WithSpend.class);
            SpendDto input = SpendDto.builder()
                    .spendDate(new Date())
                    .category(annotation.category())
                    .currency(annotation.currency())
                    .amount(annotation.amount())
                    .description(annotation.description())
                    .username(annotation.username())
                    .build();

            SpendDto spend = SpendApi.add(input);

            field.setAccessible(true);
            field.set(testInstance, spend);
            spends.add(spend);

            String testIdentifier = getTestIdentifier(extensionContext);
            extensionContext.getStore(NAMESPACE).put(testIdentifier, spends);
        }
    }

    @Override
    public void afterEach(ExtensionContext extensionContext) throws Exception {
        String testIdentifier = getTestIdentifier(extensionContext);
        List<SpendDto> spends = extensionContext.getStore(NAMESPACE).get(testIdentifier, List.class);
        SpendApi.delete(spends);
    }

    private String getTestIdentifier(ExtensionContext extensionContext) {
        return extensionContext.getRequiredTestClass().getName() + ":" +
                extensionContext.getRequiredTestMethod().getName();
    }
}
