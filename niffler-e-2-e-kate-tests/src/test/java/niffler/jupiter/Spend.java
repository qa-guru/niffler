package niffler.jupiter;

import niffler.model.Currency;
import org.junit.jupiter.api.extension.ExtendWith;

import java.lang.annotation.Target;
import java.lang.annotation.Retention;
import java.lang.annotation.ElementType;
import java.lang.annotation.RetentionPolicy;

@ExtendWith(SpendExtension.class)
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Spend {

    String category();

    Currency currency();

    double amount();

    String description();

    String username();
}