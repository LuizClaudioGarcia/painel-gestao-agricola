package br.com.agrotis.painel.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Falha de um campo do contrato. campo é o nome do JSON ou do query param.")
public record ErroCampo(
        @Schema(example = "quantidadeMinima")
        String campo,
        @Schema(example = "menor que 1")
        String mensagem
) {
}
