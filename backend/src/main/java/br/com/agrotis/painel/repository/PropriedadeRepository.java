package br.com.agrotis.painel.repository;

import br.com.agrotis.painel.entity.Propriedade;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface PropriedadeRepository extends JpaRepository<Propriedade, UUID> {

    List<Propriedade> findAllByOrderByCodigoAsc();
}
