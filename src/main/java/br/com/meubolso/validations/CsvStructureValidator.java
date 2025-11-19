package br.com.meubolso.validations;

import br.com.meubolso.enums.CategoriaFinanceira;
import java.util.regex.Pattern;

public final class CsvStructureValidator {
    private CsvStructureValidator() {}

    public static boolean isHeaderLine(String trimmed) {
        if (trimmed == null) return false;
        String s = trimmed.replace("\uFEFF", "").toLowerCase();
        return s.startsWith("data,") || s.startsWith("data;");
    }

    public static char expectedDelimiter(CategoriaFinanceira tipo) {
        return tipo == CategoriaFinanceira.CONTACORRENTE ? ';' : ',';
    }

    public static void validateColumnCount(String line, CategoriaFinanceira tipo) {
        if (line == null || line.trim().isEmpty()) {
            throw new IllegalArgumentException("linha vazia");
        }
        char delim = expectedDelimiter(tipo);
        String[] parts = line.split(Pattern.quote(String.valueOf(delim)));
        if (parts.length < 3) {
            throw new IllegalArgumentException("linha inválida: esperado ao menos 3 colunas");
        }
    }
}