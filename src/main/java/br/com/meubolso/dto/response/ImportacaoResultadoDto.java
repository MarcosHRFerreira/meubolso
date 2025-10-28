package br.com.meubolso.dto.response;

import java.util.List;

public record ImportacaoResultadoDto(
        List<TransacaoResumoDto> inseridos,
        List<LinhaErroDto> erros
) {}