package br.com.meubolso.dto.request;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record TransacaoRequestDto(
    @NotNull(message = "data é obrigatória")
    LocalDate data,

    @NotNull(message = "valor é obrigatório")
    Double valor,

    @Size(max = 255, message = "descricao deve ter no máximo 255 caracteres")
    String descricao,

    @NotBlank(message = "tipo é obrigatório")
    @Size(max = 20, message = "tipo deve ter no máximo 20 caracteres")
    String tipo,

    @NotNull(message = "contaId é obrigatório")
    Long contaId,

    Long categoriaId
) {}