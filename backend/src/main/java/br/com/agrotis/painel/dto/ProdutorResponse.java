package br.com.agrotis.painel.dto;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

public record ProdutorResponse(
        UUID id,
        Long codigo,
        String nome,
        String inscricaoFiscal,
        String endereco,
        OffsetDateTime dataInicial,
        OffsetDateTime dataFinal,
        String observacoes,
        BigDecimal comissaoProducao,
        CadastroResumo laboratorio,
        List<CadastroResumo> propriedades
) {
}
