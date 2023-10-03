package guru.qa.niffler.jupiter;

import org.junit.jupiter.api.extension.ExtendWith;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
//@Target({ElementType.FIELD, ElementType.METHOD})
@Target(ElementType.METHOD)
@ExtendWith(GenerateCategoryExtension.class)
public @interface GenerateCategory {

    String category();

    String username();
}
