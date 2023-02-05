package niffler.jupiter.spend;

import niffler.api.base.spend.SpendApi;
import niffler.api.base.spend.dto.SpendDto;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

final class WithSpendExtension implements BeforeEachCallback, AfterEachCallback {

    @Override
    public void beforeEach(ExtensionContext extensionContext) throws Exception {
        Object testInstance = extensionContext.getRequiredTestInstance();
        List<Field> fields = Arrays.stream(testInstance.getClass().getDeclaredFields())
                .filter(f -> f.getType().isAssignableFrom(SpendDto.class))
                .filter(f -> f.isAnnotationPresent(WithSpend.class))
                .toList();

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
        }
    }

    @Override
    public void afterEach(ExtensionContext context) throws Exception {
    }

}
