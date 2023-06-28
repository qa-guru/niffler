package guru.qa.niffler.jupiter.annotation;


import guru.qa.niffler.model.rest.CurrencyValues;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface GenerateSpend {

    boolean handleAnnotation() default true;

    String spendName();

    String spendCategory();

    int addDaysToSpendDate() default 0;

    double amount();

    CurrencyValues currency() default CurrencyValues.USD;
}
