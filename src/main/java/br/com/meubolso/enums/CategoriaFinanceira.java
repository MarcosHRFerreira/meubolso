package br.com.meubolso.enums;

public enum CategoriaFinanceira {
    CONTACORRENTE,
    CARTAOCREDITO;

    public static CategoriaFinanceira fromString(String raw) {
        if (raw == null) return null;
        String s = raw.trim().toLowerCase()
                .replace("-", "")
                .replace("_", "")
                .replace(" ", "");
        switch (s) {
            case "contacorrente":
            case "contacorrent":
                return CONTACORRENTE;
            case "cartaocredito":
            case "cartao":
            case "credito":
                return CARTAOCREDITO;
            default:
                return null;
        }
    }
}
