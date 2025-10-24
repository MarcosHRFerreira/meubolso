package br.com.meubolso.validations;

import java.util.Set;
import java.util.stream.Collectors;

import br.com.meubolso.dto.request.TransacaoRequestDto;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validation;
import jakarta.validation.Validator;

public final class TransacaoRequestValidator {
    private static final Validator VALIDATOR = Validation.buildDefaultValidatorFactory().getValidator();

    private TransacaoRequestValidator() {}

    public static Set<String> validate(TransacaoRequestDto dto) {
        Set<ConstraintViolation<TransacaoRequestDto>> violations = VALIDATOR.validate(dto);
        return violations.stream()
                .map(v -> v.getPropertyPath() + ": " + v.getMessage())
                .collect(Collectors.toSet());
    }

    public static void validateOrThrow(TransacaoRequestDto dto) {
        Set<ConstraintViolation<TransacaoRequestDto>> violations = VALIDATOR.validate(dto);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
    }
}