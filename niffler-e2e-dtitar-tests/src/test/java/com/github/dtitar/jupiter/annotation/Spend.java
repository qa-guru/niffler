package com.github.dtitar.jupiter.annotation;

import com.github.dtitar.jupiter.extension.SpendExtension;
import com.github.dtitar.model.rest.CurrencyValues;
import org.junit.jupiter.api.extension.ExtendWith;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@ExtendWith(SpendExtension.class)
public @interface Spend {
    String username();
    String category();
    String description();
    double amount();
    CurrencyValues currency();
}
