package br.com.meubolso.service;

import br.com.meubolso.enums.CategoriaFinanceira;
import br.com.meubolso.dto.response.ImportacaoResultadoDto;
import org.springframework.web.multipart.MultipartFile;

public interface ImportaArquivoService {

    ImportacaoResultadoDto importar(MultipartFile file, Long contaId, CategoriaFinanceira categoriaFinanceira, String anomesref);

}
