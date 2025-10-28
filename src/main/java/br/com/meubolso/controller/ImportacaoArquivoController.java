package br.com.meubolso.controller;

import br.com.meubolso.enums.CategoriaFinanceira;
import br.com.meubolso.service.ImportaArquivoService;
import br.com.meubolso.validations.TransacaoRequestValidator;
import jakarta.validation.constraints.NotNull;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
// removed explicit OpenAPI RequestBody override to fix Swagger 500
import br.com.meubolso.dto.response.ImportacaoResultadoDto;
import org.springframework.web.multipart.MultipartFile;


@Validated
@RestController
@RequestMapping("/importacao-arquivo")
@Tag(name = "Importação de Arquivos", description = "Endpoints para importar transações por arquivo")
public class ImportacaoArquivoController {

    Logger logger = LogManager.getLogger(ImportacaoArquivoController.class);
    private final ImportaArquivoService importaArquivoService;

    public ImportacaoArquivoController(ImportaArquivoService importaArquivoService) {
        this.importaArquivoService = importaArquivoService;
    }

    @PostMapping(value = "/carga", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Importa um arquivo de transações", description = "Recebe um arquivo CSV e importa as transações para a conta informada conforme a categoria financeira.")
    public ResponseEntity<ImportacaoResultadoDto> carga(
            @RequestPart("file") @NotNull org.springframework.web.multipart.MultipartFile file,
            @RequestParam("tipo") @NotNull br.com.meubolso.enums.CategoriaFinanceira tipo,
            @RequestParam("contaId") @NotNull Long contaId,
            @RequestParam(name = "anomesref", required = false) String anomesref
    ) {
        logger.info("Importação única: contaId={}, tipo={}", contaId, tipo);
        if (tipo == CategoriaFinanceira.CARTAOCREDITO) {
            TransacaoRequestValidator.validateAnomesrefOrThrow(anomesref);
        }
        ImportacaoResultadoDto resultado = importaArquivoService.importar(
                file,
                contaId,
                tipo,
                anomesref
        );
        return ResponseEntity.ok(resultado);
    }



}
