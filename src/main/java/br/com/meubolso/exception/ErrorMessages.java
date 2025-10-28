package br.com.meubolso.exception;

public final class ErrorMessages {
    private ErrorMessages() {}

    // Validações de Importação
    public static final String FILE_REQUIRED = "file é obrigatório";
    public static final String TIPO_REQUIRED = "tipo é obrigatório";
    public static final String CONTA_ID_REQUIRED = "contaId é obrigatório";

    public static final String ANOMESREF_REQUIRED_FOR_CC = "anomesref é obrigatório para CARTAOCREDITO";
    public static final String ANOMESREF_INVALID_FORMAT = "anomesref deve estar no formato YYYY-MM (mês 01..12)";

    // Validações de Transação
    public static final String DATA_REQUIRED = "data é obrigatória";
    public static final String VALOR_REQUIRED = "valor é obrigatório";
    public static final String DESCRICAO_MAX_255 = "descricao deve ter no máximo 255 caracteres";
    public static final String TIPO_MAX_20 = "tipo deve ter no máximo 20 caracteres";

    // Handler de validação
    public static final String VALIDATION_FIELDS_ERROR_MESSAGE = "Erro de validação nos campos do corpo da requisição";
    public static final String VALIDATION_FORM_ERROR_MESSAGE = "Erro de validação nos parâmetros do formulário";
    public static final String VALIDATION_GENERIC_ERROR_MESSAGE = "Erro de validação";
}