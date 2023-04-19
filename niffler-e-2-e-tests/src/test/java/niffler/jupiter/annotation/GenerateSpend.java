package niffler.jupiter.annotation;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import niffler.model.CurrencyValues;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface GenerateSpend {

  String description();

  String username();

  String category();

  double amount();

  CurrencyValues currency();
}
