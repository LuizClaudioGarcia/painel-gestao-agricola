package br.com.agrotis.painel.repository;

import br.com.agrotis.painel.entity.EntidadeAgricola;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface EntidadeAgricolaRepository extends JpaRepository<EntidadeAgricola, UUID> {

    boolean existsByInscricaoFiscal(String inscricaoFiscal);

    @Query("""
            select count(e) > 0
            from EntidadeAgricola e
            where e.inscricaoFiscal = :inscricao
              and e.id <> :id
            """)
    boolean existsOutraComInscricao(@Param("inscricao") String inscricao, @Param("id") UUID id);
}
