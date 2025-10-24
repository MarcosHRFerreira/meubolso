package br.com.meubolso.validations;

import java.util.Set;
import java.util.stream.Collectors;

import br.com.meubolso.dto.request.ContaCorrenteRequestDto;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validation;
import jakarta.validation.Validator;

public final class ContaCorrenteRequestValidator {
    private static final Validator VALIDATOR = Validation.buildDefaultValidatorFactory().getValidator();

    private ContaCorrenteRequestValidator() {}

    public static Set<String> validate(ContaCorrenteRequestDto dto) {
        Set<ConstraintViolation<ContaCorrenteRequestDto>> violations = VALIDATOR.validate(dto);
        return violations.stream()
                .map(v -> v.getPropertyPath() + ": " + v.getMessage())
                .collect(Collectors.toSet());
    }

    public static void validateOrThrow(ContaCorrenteRequestDto dto) {
        Set<ConstraintViolation<ContaCorrenteRequestDto>> violations = VALIDATOR.validate(dto);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
    }
}