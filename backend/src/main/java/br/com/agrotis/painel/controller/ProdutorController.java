package br.com.agrotis.painel.controller;

import br.com.agrotis.painel.dto.ErroResponse;
import br.com.agrotis.painel.dto.ProdutorRequest;
import br.com.agrotis.painel.dto.ProdutorResponse;
import br.com.agrotis.painel.service.ProdutorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@Tag(name = "Produtores")
@RestController
@RequestMapping("/api/produtores")
public class ProdutorController {

    private final ProdutorService produtorService;

    public ProdutorController(ProdutorService produtorService) {
        this.produtorService = produtorService;
    }

    @Operation(summary = "Cadastra um produtor")
    @RespostasDeErro
    @PostMapping
    public ResponseEntity<ProdutorResponse> cadastrar(@Valid @RequestBody ProdutorRequest request) {
        ProdutorResponse criado = produtorService.cadastrar(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(criado.id())
                .toUri();
        return ResponseEntity.created(location).body(criado);
    }

    @Operation(summary = "Lista os produtores")
    @GetMapping
    public List<ProdutorResponse> listar() {
        return produtorService.listar();
    }

    @Operation(summary = "Busca um produtor para edição")
    @ApiResponses({
            @ApiResponse(responseCode = "400", description = "O id não é um UUID.", content = @Content(schema = @Schema(implementation = ErroResponse.class))),
            @ApiResponse(responseCode = "404", description = "Não existe produtor com esse UUID.", content = @Content(schema = @Schema(implementation = ErroResponse.class)))
    })
    @GetMapping("/{id}")
    public ProdutorResponse buscar(
            @Parameter(description = "UUID do produtor", example = "00000000-0000-7000-8000-000000000021")
            @PathVariable UUID id
    ) {
        return produtorService.buscar(id);
    }

    @Operation(summary = "Substitui os dados e os vínculos do produtor")
    @RespostasDeErro
    @PutMapping("/{id}")
    public ProdutorResponse atualizar(
            @Parameter(description = "UUID do produtor", example = "00000000-0000-7000-8000-000000000021")
            @PathVariable UUID id,
            @Valid @RequestBody ProdutorRequest request
    ) {
        return produtorService.atualizar(id, request);
    }

    @Operation(summary = "Exclui o produtor")
    @ApiResponses({
            @ApiResponse(responseCode = "400", description = "O id não é um UUID.", content = @Content(schema = @Schema(implementation = ErroResponse.class))),
            @ApiResponse(responseCode = "404", description = "Não existe produtor com esse UUID.", content = @Content(schema = @Schema(implementation = ErroResponse.class)))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(
            @Parameter(description = "UUID do produtor", example = "00000000-0000-7000-8000-000000000021")
            @PathVariable UUID id
    ) {
        produtorService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
