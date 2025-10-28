package br.com.meubolso.validations;

import br.com.meubolso.dto.request.ImportacaoCargaForm;
import br.com.meubolso.enums.CategoriaFinanceira;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import br.com.meubolso.exception.ErrorMessages;

// removed pattern validation to avoid duplication with field-level @Pattern

public class ValidAnomesrefForCartaoCreditoFormValidator implements ConstraintValidator<ValidAnomesrefForCartaoCredito, ImportacaoCargaForm> {

    @Override
    public boolean isValid(ImportacaoCargaForm value, ConstraintValidatorContext context) {
        if (value == null) return true;

        if (value.getTipo() == CategoriaFinanceira.CARTAOCREDITO) {
            String ref = value.getAnomesref();
            if (ref == null || ref.trim().isEmpty()) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate(ErrorMessages.ANOMESREF_REQUIRED_FOR_CC)
                        .addPropertyNode("anomesref").addConstraintViolation();
                return false;
            }
        }
        return true;
    }
}