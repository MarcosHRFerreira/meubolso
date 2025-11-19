package br.com.meubolso.controller;

import br.com.meubolso.dto.request.TransacaoRequestDto;
import br.com.meubolso.dto.response.TransacaoResumoDto;
import br.com.meubolso.enums.CategoriaFinanceira;
import br.com.meubolso.service.TransacaoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/transacoes")
@Tag(name = "Transações", description = "Endpoints para criar transações diretamente")
public class TransacaoController {

    private final TransacaoService transacaoService;

    public TransacaoController(TransacaoService transacaoService) {
        this.transacaoService = transacaoService;
    }

    @PostMapping
    @Operation(summary = "Cria uma transação", description = "Cria uma transação a partir de DTO. Quando categoria for CARTAOCREDITO, exige anomesref.")
    public ResponseEntity<TransacaoResumoDto> criar(
            @Valid @RequestBody TransacaoRequestDto dto,
            @RequestParam("tipo") CategoriaFinanceira tipo,
            @RequestParam(name = "anomesref", required = false) String anomesref
    ) {
        TransacaoResumoDto resposta = transacaoService.criar(dto, tipo, anomesref);
        return ResponseEntity.status(201).body(resposta);
    }
}