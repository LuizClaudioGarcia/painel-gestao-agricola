package br.com.agrotis.painel.repository;

import br.com.agrotis.painel.entity.Laboratorio;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface LaboratorioRepository extends JpaRepository<Laboratorio, UUID> {

    List<Laboratorio> findAllByOrderByCodigoAsc();
}
