package br.com.meubolso.dto.response;

public record LinhaErroDto(
        int linhaNumero,
        String linhaConteudo,
        String motivo
) {}