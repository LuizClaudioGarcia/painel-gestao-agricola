package br.com.agrotis.painel.service;

import br.com.agrotis.painel.dto.FiltroLaboratorioAnalitico;
import br.com.agrotis.painel.dto.LaboratorioAnaliticoResponse;
import br.com.agrotis.painel.exception.RegraNegocioException;
import br.com.agrotis.painel.repository.LaboratorioAnaliticoConsulta;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;

@Service
public class LaboratorioAnaliticoService {

    private final LaboratorioAnaliticoConsulta consulta;

    public LaboratorioAnaliticoService(LaboratorioAnaliticoConsulta consulta) {
        this.consulta = consulta;
    }

    @Transactional(readOnly = true)
    public List<LaboratorioAnaliticoResponse> consolidar(FiltroLaboratorioAnalitico filtro) {
        if (filtro.quantidadeMinima() == null || filtro.quantidadeMinima() < 1) {
            throw new RegraNegocioException("quantidadeMinima começa em 1.");
        }
        if (invertido(filtro.dataInicialDe(), filtro.dataInicialAte())) {
            throw new RegraNegocioException("dataInicialDe não pode ser posterior a dataInicialAte.");
        }
        if (invertido(filtro.dataFinalDe(), filtro.dataFinalAte())) {
            throw new RegraNegocioException("dataFinalDe não pode ser posterior a dataFinalAte.");
        }

        boolean ordenarPelaDataInicial = filtro.dataInicialDe() != null || filtro.dataInicialAte() != null;
        return consulta.consultar(
                filtro.dataInicialDe(),
                filtro.dataInicialAte(),
                filtro.dataFinalDe(),
                filtro.dataFinalAte(),
                escapar(filtro.observacoes()),
                filtro.quantidadeMinima(),
                ordenarPelaDataInicial
        );
    }

    private boolean invertido(OffsetDateTime inicio, OffsetDateTime fim) {
        return inicio != null && fim != null && inicio.isAfter(fim);
    }

    private String escapar(String termo) {
        if (termo == null || termo.isBlank()) {
            return null;
        }
        return termo.trim()
                .replace("\\", "\\\\")
                .replace("%", "\\%")
                .replace("_", "\\_");
    }
}
