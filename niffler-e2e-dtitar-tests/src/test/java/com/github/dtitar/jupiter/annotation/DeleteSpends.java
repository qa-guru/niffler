package com.github.dtitar.jupiter.annotation;

import com.github.dtitar.jupiter.extension.DeleteSpendsExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@ExtendWith(DeleteSpendsExtension.class)
public @interface DeleteSpends {
    String username();
}
