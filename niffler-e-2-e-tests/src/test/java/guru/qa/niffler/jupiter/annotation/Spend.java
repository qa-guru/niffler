package guru.qa.niffler.jupiter.annotation;

import guru.qa.niffler.jupiter.converter.SpendConverter;
import org.junit.jupiter.params.converter.ConvertWith;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
@ConvertWith(SpendConverter.class)
public @interface Spend {
}
