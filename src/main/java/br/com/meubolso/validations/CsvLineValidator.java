package br.com.meubolso.validations;

import br.com.meubolso.enums.CategoriaFinanceira;
import br.com.meubolso.exception.ErrorMessages;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public final class CsvLineValidator {
    private CsvLineValidator() {}

    public static LocalDate parseDateForTipo(String raw, CategoriaFinanceira tipo) {
        if (raw == null || raw.trim().isEmpty()) throw new IllegalArgumentException(ErrorMessages.DATA_REQUIRED);
        String s = raw.trim();
        try {
            if (tipo == CategoriaFinanceira.CONTACORRENTE) {
                return LocalDate.parse(s, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            } else {
                return LocalDate.parse(s, DateTimeFormatter.ISO_LOCAL_DATE);
            }
        } catch (DateTimeParseException ex) {
            throw new IllegalArgumentException("formato de data inválido para " + tipo + ": " + s);
        }
    }

    public static Double parseDecimalToDouble(String raw) {
        if (raw == null || raw.trim().isEmpty()) return null;
        String s = raw.trim().replace(" ", "");
        boolean hasComma = s.contains(",");
        boolean hasDot = s.contains(".");
        if (hasComma && hasDot) {
            int lastComma = s.lastIndexOf(',');
            int lastDot = s.lastIndexOf('.');
            char decimalSep = lastComma > lastDot ? ',' : '.';
            char thousandSep = decimalSep == ',' ? '.' : ',';
            s = s.replace(String.valueOf(thousandSep), "");
            if (decimalSep == ',') s = s.replace(',', '.');
            return Double.parseDouble(s);
        } else if (hasComma) {
            s = s.replace(',', '.');
            return Double.parseDouble(s);
        } else {
            return Double.parseDouble(s);
        }
    }

    public static void validateDescricaoMaxLength(String descricao, int max) {
        if (descricao != null && descricao.length() > max) {
            throw new IllegalArgumentException(ErrorMessages.DESCRICAO_MAX_255);
        }
    }

    public static void validateTipoMaxLength(String tipo, int max) {
        if (tipo != null && tipo.length() > max) {
            throw new IllegalArgumentException(ErrorMessages.TIPO_MAX_20);
        }
    }
}