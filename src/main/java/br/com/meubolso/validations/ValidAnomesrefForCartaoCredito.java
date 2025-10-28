package br.com.meubolso.validations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import br.com.meubolso.exception.ErrorMessages;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Constraint(validatedBy = {ValidAnomesrefForCartaoCreditoValidator.class, ValidAnomesrefForCartaoCreditoFormValidator.class})
@Target({ TYPE })
@Retention(RUNTIME)
public @interface ValidAnomesrefForCartaoCredito {
    String message() default ErrorMessages.ANOMESREF_REQUIRED_FOR_CC;
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}