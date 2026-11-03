package br.com.agrotis.painel.dto;

import java.time.OffsetDateTime;

public record FiltroLaboratorioAnalitico(
        Integer quantidadeMinima,
        OffsetDateTime dataInicialDe,
        OffsetDateTime dataInicialAte,
        OffsetDateTime dataFinalDe,
        OffsetDateTime dataFinalAte,
        String observacoes
) {
}
