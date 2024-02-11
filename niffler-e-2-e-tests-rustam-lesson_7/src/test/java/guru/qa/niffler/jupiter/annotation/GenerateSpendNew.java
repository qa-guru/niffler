package guru.qa.niffler.jupiter.annotation;

import guru.qa.niffler.db.entity.userdata.CurrencyValues;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface GenerateSpendNew {

  String date() default "";
  String category();
  CurrencyValues currency() default CurrencyValues.RUB;
  double amount();
  String description() default "";

}