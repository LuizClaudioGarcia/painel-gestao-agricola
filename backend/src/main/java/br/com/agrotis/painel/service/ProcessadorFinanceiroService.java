package br.com.agrotis.painel.service;

import br.com.agrotis.painel.financeiro.CalculavelFinanceiro;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class ProcessadorFinanceiroService {

    public BigDecimal processar(CalculavelFinanceiro cadastro) {
        return cadastro.calcularValorFinanceiro();
    }
}
