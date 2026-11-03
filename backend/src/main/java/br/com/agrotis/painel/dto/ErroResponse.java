package br.com.agrotis.painel.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "Erro da API. mensagem é o tipo da falha. erros aponta o campo do contrato.")
public record ErroResponse(
        @Schema(example = "Dados inválidos")
        String mensagem,
        List<ErroCampo> erros
) {
}
