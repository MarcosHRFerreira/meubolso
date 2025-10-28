package br.com.meubolso.validations;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.regex.Pattern;

import br.com.meubolso.dto.request.TransacaoRequestDto;
import br.com.meubolso.exception.ErrorMessages;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validation;
import jakarta.validation.Validator;

public final class TransacaoRequestValidator {
    private static final Validator VALIDATOR = Validation.buildDefaultValidatorFactory().getValidator();
    private static final Pattern ANOMESREF_PATTERN = Pattern.compile("^\\d{4}-(0[1-9]|1[0-2])$");

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

    /**
     * Valida o parâmetro anomesref no formato YYYY-MM, garantindo mês entre 01 e 12.
     * Lança IllegalArgumentException com mensagem clara em caso de inválido.
     */
    public static void validateAnomesrefOrThrow(String anomesref) {
        if (anomesref == null || anomesref.trim().isEmpty()) {
            throw new IllegalArgumentException(ErrorMessages.ANOMESREF_REQUIRED_FOR_CC);
        }
        String ref = anomesref.trim();
        if (!ANOMESREF_PATTERN.matcher(ref).matches()) {
            throw new IllegalArgumentException(ErrorMessages.ANOMESREF_INVALID_FORMAT);
        }
    }

    /**
     * Retorna true se anomesref estiver no formato YYYY-MM com mês válido (01..12).
     */
    public static boolean isValidAnomesref(String anomesref) {
        return anomesref != null && ANOMESREF_PATTERN.matcher(anomesref.trim()).matches();
    }
}