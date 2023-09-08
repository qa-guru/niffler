package com.github.dtitar.jupiter.annotation;

import com.github.dtitar.jupiter.extension.CategoryExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@ExtendWith(CategoryExtension.class)
public @interface Category {

    String username();
    String description();
}
