package br.com.agrotis.painel.controller;

import br.com.agrotis.painel.dto.ErroResponse;
import br.com.agrotis.painel.dto.ValorFinanceiroResponse;
import br.com.agrotis.painel.service.ValorFinanceiroService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@Tag(name = "Financeiro")
@RestController
@RequestMapping("/api/entidades")
public class EntidadeFinanceiroController {

    private final ValorFinanceiroService valorFinanceiroService;

    public EntidadeFinanceiroController(ValorFinanceiroService valorFinanceiroService) {
        this.valorFinanceiroService = valorFinanceiroService;
    }

    @Operation(summary = "Processa o valor financeiro do cadastro", description = "Produtor devolve a comissão. Propriedade devolve a taxa territorial. Laboratório devolve a taxa operacional. A rota não envia valor e o service não verifica o tipo.")
    @ApiResponses({
            @ApiResponse(responseCode = "400", description = "O id não é um UUID.", content = @Content(schema = @Schema(implementation = ErroResponse.class))),
            @ApiResponse(responseCode = "404", description = "Não existe cadastro com esse UUID.", content = @Content(schema = @Schema(implementation = ErroResponse.class)))
    })
    @GetMapping("/{id}/valor-financeiro")
    public ValorFinanceiroResponse consultar(
            @Parameter(description = "UUID do produtor, da propriedade ou do laboratório", example = "00000000-0000-7000-8000-000000000021")
            @PathVariable UUID id
    ) {
        return valorFinanceiroService.consultar(id);
    }
}
