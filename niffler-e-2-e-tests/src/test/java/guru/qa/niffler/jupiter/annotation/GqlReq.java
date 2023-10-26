package guru.qa.niffler.jupiter.annotation;

import guru.qa.niffler.jupiter.converter.GqlReqConverter;
import org.junit.jupiter.params.converter.ConvertWith;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
@ConvertWith(GqlReqConverter.class)
public @interface GqlReq {
    String value() default ""; // for parametrized tests
}
