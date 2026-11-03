package br.com.agrotis.painel.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "Erro da API. A mensagem explica o problema. A lista traz o campo quando a falha é de validação.")
public record ErroResponse(
        @Schema(example = "O id precisa ser um UUID. Exemplo: 00000000-0000-7000-8000-000000000021")
        String mensagem,
        List<String> erros
) {
}
