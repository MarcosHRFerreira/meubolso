package br.com.meubolso.service.impl;

import br.com.meubolso.dto.request.TransacaoRequestDto;
import br.com.meubolso.dto.response.TransacaoResumoDto;
import br.com.meubolso.entity.ContaCorrenteEntity;
import br.com.meubolso.entity.TransacaoEntity;
import br.com.meubolso.enums.CategoriaFinanceira;
import br.com.meubolso.exception.DuplicateTransactionException;
import br.com.meubolso.mapper.TransacaoMapper;
import br.com.meubolso.repository.ContaCorrenteRepository;
import br.com.meubolso.repository.TransacaoRepository;
import br.com.meubolso.service.TransacaoService;
import br.com.meubolso.validations.TransacaoRequestValidator;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class TransacaoServiceImpl implements TransacaoService {

    private final TransacaoRepository transacaoRepository;
    private final ContaCorrenteRepository contaCorrenteRepository;
    private final TransacaoMapper transacaoMapper;

    public TransacaoServiceImpl(TransacaoRepository transacaoRepository,
                                ContaCorrenteRepository contaCorrenteRepository,
                                TransacaoMapper transacaoMapper) {
        this.transacaoRepository = transacaoRepository;
        this.contaCorrenteRepository = contaCorrenteRepository;
        this.transacaoMapper = transacaoMapper;
    }

    @Override
    public TransacaoResumoDto criar(TransacaoRequestDto dto, CategoriaFinanceira tipo, String anomesref) {
        if (tipo == CategoriaFinanceira.CARTAOCREDITO) {
            TransacaoRequestValidator.validateAnomesrefOrThrow(anomesref);
        }

        ContaCorrenteEntity conta = contaCorrenteRepository.findById(dto.contaId())
                .orElseThrow(() -> new EntityNotFoundException("ContaCorrente não encontrada: id=" + dto.contaId()));

        TransacaoEntity entity = transacaoMapper.toEntity(dto, conta, tipo, anomesref);

        boolean existe = transacaoRepository
                .existsByContaCorrente_IdAndDataAndDescricaoAndValorAndTipoAndCategoria(
                        conta.getId(),
                        entity.getData(),
                        entity.getDescricao(),
                        entity.getValor(),
                        entity.getTipo(),
                        entity.getCategoria()
                );
        if (existe) {
            throw new DuplicateTransactionException("Transação duplicada");
        }

        TransacaoEntity salvo = transacaoRepository.save(entity);
        return transacaoMapper.toResumoDto(salvo, conta.getId());
    }
}