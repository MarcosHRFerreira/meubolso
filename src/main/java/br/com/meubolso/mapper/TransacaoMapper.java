package br.com.meubolso.mapper;

import org.mapstruct.AfterMapping;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import br.com.meubolso.dto.request.TransacaoRequestDto;
import br.com.meubolso.dto.response.TransacaoResumoDto;
import br.com.meubolso.entity.ContaCorrenteEntity;
import br.com.meubolso.entity.TransacaoEntity;
import br.com.meubolso.enums.CategoriaFinanceira;

@Mapper(componentModel = "spring")
public interface TransacaoMapper {

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    TransacaoEntity toEntity(TransacaoRequestDto dto,
                             ContaCorrenteEntity contaCorrente,
                             CategoriaFinanceira categoria,
                             String anomesref);

    @Mapping(target = "contaId", expression = "java(contaId != null ? contaId : (entity.getContaCorrente() != null ? entity.getContaCorrente().getId() : null))")
    TransacaoResumoDto toResumoDto(TransacaoEntity entity, Long contaId);

    @AfterMapping
    default void setAnomesrefIfCartao(TransacaoRequestDto dto,
                                      ContaCorrenteEntity contaCorrente,
                                      CategoriaFinanceira categoria,
                                      String anomesref,
                                      @MappingTarget TransacaoEntity entity) {
        // Define conta e categoria provenientes dos parâmetros auxiliares
        if (contaCorrente != null) {
            entity.setContaCorrente(contaCorrente);
        }
        if (categoria != null) {
            entity.setCategoria(categoria);
        }

        // Trata anomesref apenas para CARTAOCREDITO
        if (categoria == CategoriaFinanceira.CARTAOCREDITO && anomesref != null) {
            entity.setAnomesref(anomesref.trim());
        }

        // Sanitiza descrição quando presente
        if (dto != null && dto.descricao() != null) {
            entity.setDescricao(br.com.meubolso.validations.SanitizationUtils.sanitizeDescription(dto.descricao()));
        }

        // Mapeia tipo apenas quando não for vazio
        if (dto != null && dto.tipo() != null && !dto.tipo().isBlank()) {
            entity.setTipo(dto.tipo());
        }
    }
}