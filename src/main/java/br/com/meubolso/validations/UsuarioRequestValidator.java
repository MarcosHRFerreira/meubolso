package br.com.meubolso.validations;

import br.com.meubolso.dto.request.UsuarioRequestDto;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import java.util.Set;

public final class UsuarioRequestValidator {

    private static final ValidatorFactory FACTORY = Validation.buildDefaultValidatorFactory();
    private static final Validator VALIDATOR = FACTORY.getValidator();

    private UsuarioRequestValidator() {}

    public static Set<ConstraintViolation<UsuarioRequestDto>> validate(UsuarioRequestDto dto) {
        return VALIDATOR.validate(dto);
    }

    public static void validateOrThrow(UsuarioRequestDto dto) {
        Set<ConstraintViolation<UsuarioRequestDto>> violations = validate(dto);
        if (!violations.isEmpty()) {
            StringBuilder sb = new StringBuilder("Dados inválidos para UsuarioRequestDto: ");
            for (ConstraintViolation<UsuarioRequestDto> v : violations) {
                sb.append("[")
                  .append(v.getPropertyPath())
                  .append(": ")
                  .append(v.getMessage())
                  .append("] ");
            }
            throw new IllegalArgumentException(sb.toString());
        }
    }
}