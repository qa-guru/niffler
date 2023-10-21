package guru.qa.niffler.jupiter.annotation;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
public @interface GenerateUser {

    boolean handleAnnotation() default true;

    String username() default "";

    String password() default "";

    GenerateCategory[] categories() default {};

    GenerateSpend[] spends() default {};

    Friends friends() default @Friends(handleAnnotation = false);

    IncomeInvitations incomeInvitations() default @IncomeInvitations(handleAnnotation = false);

    OutcomeInvitations outcomeInvitations() default @OutcomeInvitations(handleAnnotation = false);
}
