package br.com.meubolso.validations;

public final class SanitizationUtils {
    private SanitizationUtils() {}

    public static String sanitizeDescription(String desc) {
        if (desc == null) return null;
        String s = desc.replace("\uFEFF", "").trim();
        s = s.replaceAll("\\p{Cntrl}", "");
        s = s.replaceAll("\\s+", " ").trim();
        return s;
    }
}