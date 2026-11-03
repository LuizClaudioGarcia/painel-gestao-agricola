package br.com.agrotis.painel.service;

import br.com.agrotis.painel.dto.CadastroResumo;
import br.com.agrotis.painel.repository.LaboratorioRepository;
import br.com.agrotis.painel.repository.PropriedadeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SeletorService {

    private final LaboratorioRepository laboratorioRepository;
    private final PropriedadeRepository propriedadeRepository;

    public SeletorService(LaboratorioRepository laboratorioRepository, PropriedadeRepository propriedadeRepository) {
        this.laboratorioRepository = laboratorioRepository;
        this.propriedadeRepository = propriedadeRepository;
    }

    @Transactional(readOnly = true)
    public List<CadastroResumo> listarLaboratorios() {
        return laboratorioRepository.findAllByOrderByCodigoAsc().stream()
                .map(CadastroResumo::de)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<CadastroResumo> listarPropriedades() {
        return propriedadeRepository.findAllByOrderByCodigoAsc().stream()
                .map(CadastroResumo::de)
                .toList();
    }
}
