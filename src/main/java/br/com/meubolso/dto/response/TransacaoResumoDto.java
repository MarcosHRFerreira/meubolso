package br.com.meubolso.dto.response;

import br.com.meubolso.enums.CategoriaFinanceira;
import java.time.LocalDate;

public record TransacaoResumoDto(
        Long id,
        Long contaId,
        LocalDate data,
        String descricao,
        Double valor,
        String tipo,
        CategoriaFinanceira categoria,
        String anomesref
) {}