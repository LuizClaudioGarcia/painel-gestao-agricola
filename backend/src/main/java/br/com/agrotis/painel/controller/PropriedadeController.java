package br.com.agrotis.painel.controller;

import br.com.agrotis.painel.dto.CadastroResumo;
import br.com.agrotis.painel.service.SeletorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Propriedades")
@RestController
@RequestMapping("/api/propriedades")
public class PropriedadeController {

    private final SeletorService seletorService;

    public PropriedadeController(SeletorService seletorService) {
        this.seletorService = seletorService;
    }

    @Operation(summary = "Lista as propriedades para o seletor")
    @GetMapping
    public List<CadastroResumo> listar() {
        return seletorService.listarPropriedades();
    }
}
