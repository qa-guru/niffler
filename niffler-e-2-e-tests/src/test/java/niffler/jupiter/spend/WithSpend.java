package niffler.jupiter.spend;

import niffler.models.Category;
import niffler.models.Currency;
import org.junit.jupiter.api.extension.ExtendWith;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@ExtendWith(WithSpendExtension.class)
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface WithSpend {
    Category category();
    Currency currency() default Currency.RUB;
    double amount();
    String description() default "Опять хрень купил...";
    String username();
}
