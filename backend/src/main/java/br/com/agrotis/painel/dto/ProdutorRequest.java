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
        @NotBlank(message = "obrigatório")
        @Size(max = 150, message = "máximo de 150 caracteres")
        @Schema(example = "Joana da Silva")
        String nome,

        @NotBlank(message = "obrigatório")
        @Size(max = 32, message = "máximo de 32 caracteres")
        @Schema(example = "701.888.999-10")
        String inscricaoFiscal,

        @NotBlank(message = "obrigatório")
        @Size(max = 255, message = "máximo de 255 caracteres")
        @Schema(example = "Rua Nova, 10")
        String endereco,

        @NotNull(message = "obrigatório")
        @Schema(example = "2026-07-01T08:00:00Z")
        OffsetDateTime dataInicial,

        @NotNull(message = "obrigatório")
        @Schema(example = "2026-07-10T18:00:00Z")
        OffsetDateTime dataFinal,

        @NotNull(message = "obrigatório")
        @DecimalMin(value = "0.00", message = "valor negativo")
        @Digits(integer = 13, fraction = 2, message = "máximo de 2 casas decimais")
        @Schema(example = "900.00")
        BigDecimal comissaoProducao,

        @Schema(example = "Cadastro feito pelo Swagger")
        String observacoes,

        @NotNull(message = "obrigatório")
        @Schema(description = "UUID do laboratório. Pegue em GET /api/laboratorios.", example = "00000000-0000-7000-8000-000000000002")
        UUID laboratorioId,

        @Schema(description = "UUIDs das propriedades. Lista vazia deixa o produtor sem fazenda.", example = "[\"00000000-0000-7000-8000-000000000013\"]")
        List<UUID> propriedadesIds
) {
}
