package br.com.agrotis.painel.repository;

import br.com.agrotis.painel.entity.Produtor;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProdutorRepository extends JpaRepository<Produtor, UUID> {

    @EntityGraph(attributePaths = {"laboratorio", "propriedades"})
    @Query("select p from Produtor p order by p.codigo")
    List<Produtor> listarParaGrade();

    @EntityGraph(attributePaths = {"laboratorio", "propriedades"})
    @Query("select p from Produtor p where p.id = :id")
    Optional<Produtor> buscarComVinculos(@Param("id") UUID id);
}
