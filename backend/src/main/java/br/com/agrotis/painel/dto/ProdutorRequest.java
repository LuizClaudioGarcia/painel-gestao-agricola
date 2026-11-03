package br.com.agrotis.painel.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

public record ProdutorRequest(
        @NotBlank(message = "informe o nome")
        @Size(max = 150, message = "use no máximo 150 caracteres")
        @Schema(example = "Joana da Silva")
        String nome,

        @NotBlank(message = "informe a inscrição fiscal")
        @Size(max = 32, message = "use no máximo 32 caracteres")
        @Schema(example = "701.888.999-10")
        String inscricaoFiscal,

        @NotBlank(message = "informe o endereço")
        @Size(max = 255, message = "use no máximo 255 caracteres")
        @Schema(example = "Rua Nova, 10")
        String endereco,

        @NotNull(message = "informe a data inicial")
        @Schema(example = "2026-07-01T08:00:00Z")
        OffsetDateTime dataInicial,

        @NotNull(message = "informe a data final")
        @Schema(example = "2026-07-10T18:00:00Z")
        OffsetDateTime dataFinal,

        @NotNull(message = "informe a comissão")
        @DecimalMin(value = "0.00", message = "não pode ser negativa")
        @Digits(integer = 13, fraction = 2, message = "use no máximo 2 casas decimais")
        @Schema(example = "900.00")
        BigDecimal comissaoProducao,

        @Schema(example = "Cadastro feito pelo Swagger")
        String observacoes,

        @NotNull(message = "informe o laboratório")
        @Schema(description = "UUID do laboratório. Pegue em GET /api/laboratorios.", example = "00000000-0000-7000-8000-000000000002")
        UUID laboratorioId,

        @Schema(description = "UUIDs das propriedades. Lista vazia deixa o produtor sem fazenda.", example = "[\"00000000-0000-7000-8000-000000000013\"]")
        List<UUID> propriedadesIds
) {
}
