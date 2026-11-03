package br.com.agrotis.painel.laboratorio;

import br.com.agrotis.painel.dto.LaboratorioAnaliticoResponse;
import br.com.agrotis.painel.dto.ValorFinanceiroResponse;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.jdbc.core.JdbcTemplate;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class LaboratorioAnaliticoIT {

    private static final String LAB_A = "a0000000-0000-7000-8000-000000000010";
    private static final String LAB_B = "a0000000-0000-7000-8000-000000000020";
    private static final String LAB_C = "a0000000-0000-7000-8000-000000000030";

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16");

    @Autowired
    private JdbcTemplate jdbc;

    @Autowired
    private TestRestTemplate rest;

    @BeforeAll
    void carregarFixture() {
        inserirLaboratorio(LAB_A, 10, "10.00", "Laboratório Água", "FIX-LAB-A");
        inserirLaboratorio(LAB_B, 20, "20.00", "Laboratório Brisa", "FIX-LAB-B");
        inserirLaboratorio(LAB_C, 30, "30.00", "Laboratório Cedro", "FIX-LAB-C");

        inserirProdutor("a0000000-0000-7000-8000-000000000011", 101, LAB_A, "2026-04-02T08:00:00Z", "2026-04-10T18:00:00Z", "FIX-PROD-A1");
        inserirProdutor("a0000000-0000-7000-8000-000000000012", 102, LAB_A, "2026-05-01T08:00:00Z", "2026-08-01T18:00:00Z", "FIX-PROD-A2");
        inserirProdutor("a0000000-0000-7000-8000-000000000021", 201, LAB_B, "2026-01-10T08:00:00Z", "2026-02-01T18:00:00Z", "FIX-PROD-B1");
        inserirProdutor("a0000000-0000-7000-8000-000000000022", 202, LAB_B, "2026-03-01T08:00:00Z", "2026-03-20T18:00:00Z", "FIX-PROD-B2");
        inserirProdutor("a0000000-0000-7000-8000-000000000031", 301, LAB_C, "2026-02-01T08:00:00Z", "2026-02-05T18:00:00Z", "FIX-PROD-C");

        jdbc.update("""
                insert into entidade_agricola (id, nome, inscricao_fiscal, endereco)
                values ('a0000000-0000-7000-8000-0000000000aa', 'Fazenda Compartilhada', 'FIX-FAZ-A', 'Rua da Fixture, 1')
                """);
        jdbc.update("""
                insert into propriedade (id, codigo, taxa_territorial)
                values ('a0000000-0000-7000-8000-0000000000aa', 101, 5.00)
                """);
        jdbc.update("""
                insert into produtor_propriedade (produtor_id, propriedade_id) values
                ('a0000000-0000-7000-8000-000000000011', 'a0000000-0000-7000-8000-0000000000aa'),
                ('a0000000-0000-7000-8000-000000000012', 'a0000000-0000-7000-8000-0000000000aa')
                """);
    }

    @Test
    void consultaPrincipalFiltraContaEOrdena() {
        List<LaboratorioAnaliticoResponse> empate = consultar(2, "fixture-ordem", null, null);

        assertEquals(2, empate.size());
        assertEquals(10L, empate.get(0).codigo());
        assertEquals("LABORATÓRIO ÁGUA", empate.get(0).nome());
        assertEquals(20L, empate.get(1).codigo());
        assertEquals(0, empate.get(1).valor().compareTo(new BigDecimal("20.00")));
        assertTrue(empate.stream().noneMatch(linha -> linha.codigo() == 30L));

        List<LaboratorioAnaliticoResponse> pelaData = consultar(2, "fixture-ordem", "2026-01-01T00:00:00Z", null);
        assertEquals(20L, pelaData.get(0).codigo());
        assertEquals(10L, pelaData.get(1).codigo());

        List<LaboratorioAnaliticoResponse> semManoel = consultar(1, "fixture-ordem", null, null);
        assertTrue(semManoel.stream().noneMatch(linha -> linha.codigo() < 10L));

        List<LaboratorioAnaliticoResponse> dataFinalCortaA = consultar(1, "fixture-ordem", null, "2026-06-01T00:00:00Z");
        LaboratorioAnaliticoResponse agua = dataFinalCortaA.stream()
                .filter(linha -> linha.codigo() == 10L)
                .findFirst()
                .orElseThrow();
        assertEquals(1L, agua.quantidadeProdutores());

        ValorFinanceiroResponse valor = rest.getForObject(
                "/api/entidades/" + LAB_B + "/valor-financeiro",
                ValorFinanceiroResponse.class
        );
        assertEquals(0, valor.valor().compareTo(new BigDecimal("20.00")));
        assertEquals("Laboratorio", valor.tipo());
    }

    private List<LaboratorioAnaliticoResponse> consultar(int minimo, String observacoes, String dataInicialDe, String dataFinalDe) {
        String url = "/api/laboratorios/analitico?quantidadeMinima=" + minimo + "&observacoes=" + observacoes;
        if (dataInicialDe != null) {
            url += "&dataInicialDe=" + dataInicialDe;
        }
        if (dataFinalDe != null) {
            url += "&dataFinalDe=" + dataFinalDe;
        }
        LaboratorioAnaliticoResponse[] corpo = rest.getForObject(url, LaboratorioAnaliticoResponse[].class);
        return Arrays.asList(corpo);
    }

    private void inserirLaboratorio(String id, int codigo, String taxa, String nome, String inscricao) {
        jdbc.update(
                "insert into entidade_agricola (id, nome, inscricao_fiscal, endereco) values (?::uuid, ?, ?, ?)",
                id, nome, inscricao, "Rua da Fixture, 1"
        );
        jdbc.update(
                "insert into laboratorio (id, codigo, taxa_operacional) values (?::uuid, ?, ?)",
                id, codigo, new BigDecimal(taxa)
        );
    }

    private void inserirProdutor(String id, int codigo, String laboratorioId, String inicio, String fim, String inscricao) {
        jdbc.update(
                "insert into entidade_agricola (id, nome, inscricao_fiscal, endereco) values (?::uuid, ?, ?, ?)",
                id, "Produtor " + codigo, inscricao, "Rua da Fixture, 2"
        );
        jdbc.update("""
                insert into produtor (id, codigo, laboratorio_id, data_inicial, data_final, observacoes, comissao_producao)
                values (?::uuid, ?, ?::uuid, ?::timestamptz, ?::timestamptz, 'fixture-ordem', 1.00)
                """,
                id, codigo, laboratorioId, inicio, fim
        );
    }
}
