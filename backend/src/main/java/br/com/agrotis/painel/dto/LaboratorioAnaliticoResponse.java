package br.com.agrotis.painel.dto;

import java.math.BigDecimal;

public record LaboratorioAnaliticoResponse(
        Long codigo,
        String nome,
        Long quantidadeProdutores,
        BigDecimal valor
) {
}
