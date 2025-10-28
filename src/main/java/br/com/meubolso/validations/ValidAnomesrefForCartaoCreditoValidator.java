package br.com.meubolso.validations;

import br.com.meubolso.dto.request.ImportacaoCargaRequestDto;
import br.com.meubolso.enums.CategoriaFinanceira;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import br.com.meubolso.exception.ErrorMessages;

// removed pattern validation here to avoid duplicate errors; field-level @Pattern covers format

public class ValidAnomesrefForCartaoCreditoValidator implements ConstraintValidator<ValidAnomesrefForCartaoCredito, ImportacaoCargaRequestDto> {

    @Override
    public boolean isValid(ImportacaoCargaRequestDto value, ConstraintValidatorContext context) {
        if (value == null) return true; // outras validações cobrem null

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