package br.com.agrotis.painel.controller;

import br.com.agrotis.painel.dto.CadastroResumo;
import br.com.agrotis.painel.dto.ErroResponse;
import br.com.agrotis.painel.dto.FiltroLaboratorioAnalitico;
import br.com.agrotis.painel.dto.LaboratorioAnaliticoResponse;
import br.com.agrotis.painel.service.LaboratorioAnaliticoService;
import br.com.agrotis.painel.service.SeletorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.OffsetDateTime;
import java.util.List;

@Tag(name = "Laboratórios")
@RestController
@RequestMapping("/api/laboratorios")
public class LaboratorioController {

    private final SeletorService seletorService;
    private final LaboratorioAnaliticoService laboratorioAnaliticoService;

    public LaboratorioController(SeletorService seletorService, LaboratorioAnaliticoService laboratorioAnaliticoService) {
        this.seletorService = seletorService;
        this.laboratorioAnaliticoService = laboratorioAnaliticoService;
    }

    @Operation(summary = "Lista os laboratórios para o seletor")
    @GetMapping
    public List<CadastroResumo> listar() {
        return seletorService.listarLaboratorios();
    }

    @Operation(summary = "Consolida os laboratórios", description = "A conta roda no banco. O nome sai em maiúsculo. A fazenda não entra, para o produtor não ser contado duas vezes. A ordenação pela data inicial mais antiga só entra se dataInicialDe ou dataInicialAte vier na requisição.")
    @ApiResponses({
            @ApiResponse(responseCode = "400", description = "quantidadeMinima ausente ou menor que 1, data invertida, ou data em formato inválido.", content = @Content(schema = @Schema(implementation = ErroResponse.class)))
    })
    @GetMapping("/analitico")
    public List<LaboratorioAnaliticoResponse> consolidar(
            @Parameter(description = "Quantidade mínima de produtores. Começa em 1.", example = "1", required = true)
            @RequestParam Integer quantidadeMinima,
            @Parameter(description = "Data inicial do produtor, a partir de. Exemplo: 2026-01-01T00:00:00Z")
            @RequestParam(required = false) OffsetDateTime dataInicialDe,
            @Parameter(description = "Data inicial do produtor, até.")
            @RequestParam(required = false) OffsetDateTime dataInicialAte,
            @Parameter(description = "Data final do produtor, a partir de.")
            @RequestParam(required = false) OffsetDateTime dataFinalDe,
            @Parameter(description = "Data final do produtor, até.")
            @RequestParam(required = false) OffsetDateTime dataFinalAte,
            @Parameter(description = "Trecho das observações do produtor.")
            @RequestParam(required = false) String observacoes
    ) {
        return laboratorioAnaliticoService.consolidar(new FiltroLaboratorioAnalitico(
                quantidadeMinima,
                dataInicialDe,
                dataInicialAte,
                dataFinalDe,
                dataFinalAte,
                observacoes
        ));
    }
}
