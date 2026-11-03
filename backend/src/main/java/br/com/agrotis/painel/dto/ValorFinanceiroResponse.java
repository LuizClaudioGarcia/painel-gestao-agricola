package br.com.agrotis.painel.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record ValorFinanceiroResponse(
        UUID id,
        Long codigo,
        String nome,
        String tipo,
        BigDecimal valor
) {
}
