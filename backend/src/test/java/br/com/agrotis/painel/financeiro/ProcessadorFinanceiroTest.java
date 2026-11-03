package br.com.agrotis.painel.financeiro;

import br.com.agrotis.painel.entity.Laboratorio;
import br.com.agrotis.painel.entity.Produtor;
import br.com.agrotis.painel.entity.Propriedade;
import br.com.agrotis.painel.service.ProcessadorFinanceiroService;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ProcessadorFinanceiroTest {

    private final ProcessadorFinanceiroService service = new ProcessadorFinanceiroService();

    @Test
    void cadaTipoDevolveOValorCadastrado() {
        Produtor produtor = new Produtor();
        produtor.setComissaoProducao(new BigDecimal("1500.00"));

        Propriedade propriedade = new Propriedade();
        propriedade.setTaxaTerritorial(new BigDecimal("75.00"));

        Laboratorio laboratorio = new Laboratorio();
        laboratorio.setTaxaOperacional(new BigDecimal("250.00"));

        assertEquals(0, service.processar(produtor).compareTo(new BigDecimal("1500.00")));
        assertEquals(0, service.processar(propriedade).compareTo(new BigDecimal("75.00")));
        assertEquals(0, service.processar(laboratorio).compareTo(new BigDecimal("250.00")));
    }

    @Test
    void tipoNovoPassaPeloMesmoService() {
        CalculavelFinanceiro armazem = () -> new BigDecimal("30.00");

        assertEquals(0, service.processar(armazem).compareTo(new BigDecimal("30.00")));
    }
}
