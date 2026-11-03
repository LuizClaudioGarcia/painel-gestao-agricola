package br.com.agrotis.painel.service;

import br.com.agrotis.painel.dto.ValorFinanceiroResponse;
import br.com.agrotis.painel.entity.EntidadeAgricola;
import br.com.agrotis.painel.exception.NaoEncontradoException;
import br.com.agrotis.painel.repository.EntidadeAgricolaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class ValorFinanceiroService {

    private final EntidadeAgricolaRepository entidadeAgricolaRepository;
    private final ProcessadorFinanceiroService processadorFinanceiroService;

    public ValorFinanceiroService(
            EntidadeAgricolaRepository entidadeAgricolaRepository,
            ProcessadorFinanceiroService processadorFinanceiroService
    ) {
        this.entidadeAgricolaRepository = entidadeAgricolaRepository;
        this.processadorFinanceiroService = processadorFinanceiroService;
    }

    @Transactional(readOnly = true)
    public ValorFinanceiroResponse consultar(UUID id) {
        EntidadeAgricola cadastro = entidadeAgricolaRepository.findById(id)
                .orElseThrow(() -> new NaoEncontradoException("Cadastro não encontrado."));
        return new ValorFinanceiroResponse(
                cadastro.getId(),
                cadastro.getCodigo(),
                cadastro.getNome(),
                cadastro.getTipo(),
                processadorFinanceiroService.processar(cadastro)
        );
    }
}
