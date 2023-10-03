package guru.qa.niffler.jupiter.annotation;


import guru.qa.niffler.enums.CurrencyValues;
import guru.qa.niffler.jupiter.extension.GenerateSpendExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@ExtendWith(GenerateSpendExtension.class)
public @interface GenerateSpend {

    String username();

    String description();

    String category();

    double amount();

    CurrencyValues currency();

}
