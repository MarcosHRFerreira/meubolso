package br.com.meubolso.service;

import br.com.meubolso.dto.request.TransacaoRequestDto;
import br.com.meubolso.dto.response.TransacaoResumoDto;
import br.com.meubolso.enums.CategoriaFinanceira;

public interface TransacaoService {

    TransacaoResumoDto criar(TransacaoRequestDto dto, CategoriaFinanceira tipo, String anomesref);

}