package br.com.meubolso.dto.request;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import br.com.meubolso.exception.ErrorMessages;

public record TransacaoRequestDto(
    @NotNull(message = ErrorMessages.DATA_REQUIRED)
    LocalDate data,

    @NotNull(message = ErrorMessages.VALOR_REQUIRED)
    Double valor,

    @Size(max = 255, message = ErrorMessages.DESCRICAO_MAX_255)
    String descricao,

    @NotBlank(message = ErrorMessages.TIPO_REQUIRED)
    @Size(max = 20, message = ErrorMessages.TIPO_MAX_20)
    String tipo,

    @NotNull(message = ErrorMessages.CONTA_ID_REQUIRED)
    Long contaId,

    Long categoriaId
) {}