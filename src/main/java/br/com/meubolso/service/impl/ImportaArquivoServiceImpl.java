package br.com.meubolso.service.impl;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import br.com.meubolso.dto.response.ImportacaoResultadoDto;
import br.com.meubolso.dto.response.LinhaErroDto;
import br.com.meubolso.dto.response.TransacaoResumoDto;
import br.com.meubolso.entity.ContaCorrenteEntity;
import br.com.meubolso.entity.TransacaoEntity;
import br.com.meubolso.enums.CategoriaFinanceira;
import br.com.meubolso.mapper.TransacaoMapper;
import br.com.meubolso.repository.ContaCorrenteRepository;
import br.com.meubolso.repository.TransacaoRepository;
import br.com.meubolso.service.ImportaArquivoService;
import br.com.meubolso.validations.CsvStructureValidator;
import br.com.meubolso.validations.FileUploadValidator;
import br.com.meubolso.validations.SanitizationUtils;
import br.com.meubolso.validations.TransacaoRequestValidator;
import jakarta.persistence.EntityNotFoundException;

@Service
public class ImportaArquivoServiceImpl implements ImportaArquivoService {

    private final TransacaoRepository transacaoRepository;
    private final ContaCorrenteRepository contaCorrenteRepository;
    private final TransacaoMapper transacaoMapper;

    public ImportaArquivoServiceImpl(TransacaoRepository transacaoRepository,
                                     ContaCorrenteRepository contaCorrenteRepository,
                                     TransacaoMapper transacaoMapper) {
        this.transacaoRepository = transacaoRepository;
        this.contaCorrenteRepository = contaCorrenteRepository;
        this.transacaoMapper = transacaoMapper;
    }

    @Override
    public ImportacaoResultadoDto importar(MultipartFile file, Long contaId, CategoriaFinanceira categoriaFinanceira, String anomesref) {
        ContaCorrenteEntity conta = contaCorrenteRepository.findById(contaId)
                .orElseThrow(() -> new EntityNotFoundException("ContaCorrente não encontrada: id=" + contaId));
        // validações de titularidade foram removidas do contrato atual

        FileUploadValidator.validateCsvUploadOrThrow(file, categoriaFinanceira);
        FileUploadValidator.validateMaxLines(file, 50000);
        if (categoriaFinanceira == CategoriaFinanceira.CARTAOCREDITO) {
            TransacaoRequestValidator.validateAnomesrefOrThrow(anomesref);
        }
        try (BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {
            List<String> linhas = br.lines().collect(Collectors.toList());
            List<TransacaoResumoDto> inseridos = new java.util.ArrayList<>();
            java.util.List<LinhaErroDto> erros = new java.util.ArrayList<>();
            int index = 0;
            for (String linha : linhas) {
                index++;
                if (linha == null) continue;
                String trimmed = linha.trim();
                // Remove BOM e pula cabeçalho conhecido
                String normalized = trimmed.replace("\uFEFF", "");
                if (CsvStructureValidator.isHeaderLine(normalized)) {
                    continue;
                }
                if (trimmed.isEmpty()) continue;

                try {
                    TransacaoEntity entity = parseLinha(trimmed, categoriaFinanceira);

                    if(entity.getDescricao().contains("PAGAMENTO EFETUADO")) {
                        continue;
                    }
                    if (entity.getData() == null) {
                        erros.add(new LinhaErroDto(index, trimmed, "data inválida"));
                        continue;
                    }   

                    entity.setCategoria(categoriaFinanceira);
                    if (categoriaFinanceira == CategoriaFinanceira.CARTAOCREDITO) {
                        entity.setAnomesref(anomesref);
                    }
                    entity.setContaCorrente(conta);
                    if (entity.getTipo() == null || entity.getTipo().isEmpty()) {
                        entity.setTipo("DESPESA");
                    }
                    boolean existe = transacaoRepository
                            .existsByContaCorrente_IdAndDataAndDescricaoAndValorAndTipoAndCategoria(
                                    conta.getId(),
                                    entity.getData(),
                                    entity.getDescricao(),
                                    entity.getValor(),
                                    entity.getTipo(),
                                    entity.getCategoria()
                            );
                    if (!existe) {
                        TransacaoEntity salvo = transacaoRepository.save(entity);
                        inseridos.add(transacaoMapper.toResumoDto(salvo, conta.getId()));
                    } else {
                        erros.add(new LinhaErroDto(index, trimmed, "duplicado"));
                    }
                } catch (Exception e) {
                    erros.add(new LinhaErroDto(index, trimmed, e.getMessage() != null ? e.getMessage() : "erro ao processar"));
                }
            }
            return new ImportacaoResultadoDto(inseridos, erros);
        } catch (Exception e) {
            throw new IllegalArgumentException("Falha ao importar arquivo: " + e.getMessage(), e);
        }
    }

    private TransacaoEntity parseLinha(String linha, CategoriaFinanceira categoria) {
        if (categoria == CategoriaFinanceira.CONTACORRENTE) {
            String[] parts = linha.split(";");
            if (parts.length < 3) throw new IllegalArgumentException("Linha inválida para CONTACORRENTE: " + linha);
            LocalDate data = LocalDate.parse(parts[0].trim(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            String descricao = parts[1].trim();
            String valorRaw = parts[2].trim();
            boolean negativo = valorRaw.startsWith("-") || valorRaw.contains("-");
            Double valor = parseDecimal(valorRaw);
            if (valor != null) valor = Math.abs(valor);
            TransacaoEntity t = new TransacaoEntity();
            t.setData(data);
            t.setDescricao(SanitizationUtils.sanitizeDescription(descricao));
            t.setValor(valor);
            t.setTipo(negativo ? "DEBITO" : "CREDITO");
            return t;
        } else if (categoria == CategoriaFinanceira.CARTAOCREDITO) {
            String[] parts = linha.split(",");
            if (parts.length < 3) throw new IllegalArgumentException("Linha inválida para CARTAOCREDITO: " + linha);
            LocalDate data = LocalDate.parse(parts[0].trim(), DateTimeFormatter.ISO_LOCAL_DATE);
            Double valor = parseDecimal(parts[parts.length - 1].trim());
            String descricao;
            if (parts.length == 3) {
                descricao = parts[1].trim();
            } else {
                // Junta o miolo como descrição, preservando vírgulas internas
                StringBuilder sb = new StringBuilder();
                for (int i = 1; i < parts.length - 1; i++) {
                    if (i > 1) sb.append(",");
                    sb.append(parts[i].trim());
                }
                descricao = sb.toString();
            }
            TransacaoEntity t = new TransacaoEntity();
            t.setData(data);
            t.setDescricao(SanitizationUtils.sanitizeDescription(descricao));
            t.setValor(valor);
            t.setTipo("DEBITO");
            return t;
        }
        throw new IllegalArgumentException("Categoria financeira não suportada: " + categoria);
    }

    private Double parseDecimal(String raw) {
        if (raw == null || raw.isEmpty()) return null;
        String s = raw.trim();
        // Remove espaços
        s = s.replace(" ", "");

        boolean hasComma = s.contains(",");
        boolean hasDot = s.contains(".");

        if (hasComma && hasDot) {
            // Determina o último separador como decimal; o outro é milhar
            int lastComma = s.lastIndexOf(',');
            int lastDot = s.lastIndexOf('.');
            char decimalSep = lastComma > lastDot ? ',' : '.';
            char thousandSep = decimalSep == ',' ? '.' : ',';
            // Remove todos os separadores de milhar
            s = s.replace(String.valueOf(thousandSep), "");
            // Converte separador decimal para ponto
            if (decimalSep == ',') {
                s = s.replace(',', '.');
            }
            return Double.parseDouble(s);
        } else if (hasComma) {
            // Apenas vírgula presente: trata como decimal
            s = s.replace(',', '.');
            return Double.parseDouble(s);
        } else {
            // Apenas ponto ou nenhum: não remover ponto (é decimal)
            return Double.parseDouble(s);
        }
    }
}
