package br.com.meubolso.repository;

import br.com.meubolso.entity.TransacaoEntity;
import br.com.meubolso.enums.CategoriaFinanceira;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface TransacaoRepository extends JpaRepository<TransacaoEntity, Long> {

    boolean existsByContaCorrente_IdAndDataAndDescricaoAndValorAndTipoAndCategoria(
            Long contaId,
            LocalDate data,
            String descricao,
            Double valor,
            String tipo,
            CategoriaFinanceira categoria
    );
}
