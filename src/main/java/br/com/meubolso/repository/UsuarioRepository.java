package br.com.meubolso.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import br.com.meubolso.entity.UsuarioEntity;    


public interface UsuarioRepository extends JpaRepository<UsuarioEntity, Long> {
}
