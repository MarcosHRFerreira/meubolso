package br.com.meubolso.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import br.com.meubolso.entity.TransacaoEntity;

public interface TransacaoRepository extends JpaRepository<TransacaoEntity, Long>   {

}
