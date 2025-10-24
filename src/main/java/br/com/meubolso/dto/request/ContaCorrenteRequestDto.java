package br.com.meubolso.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

public record ContaCorrenteRequestDto(
    @NotBlank(message = "banco é obrigatório")
    @Size(max = 100, message = "banco deve ter no máximo 100 caracteres")
    String banco,

    @Size(max = 20, message = "agencia deve ter no máximo 20 caracteres")
    String agencia,

    @Size(max = 20, message = "numero deve ter no máximo 20 caracteres")
    String numero,

    @NotNull(message = "saldo é obrigatório")
    @PositiveOrZero(message = "saldo deve ser zero ou positivo")
    Double saldo,

    @NotNull(message = "usuarioId é obrigatório")
    Long usuarioId
) {}