package br.com.agrotis.painel.repository;

import br.com.agrotis.painel.dto.LaboratorioAnaliticoResponse;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Types;
import java.time.OffsetDateTime;
import java.util.List;

@Repository
public class LaboratorioAnaliticoConsulta {

    // Não junta produtor_propriedade. Fazenda repetiria o produtor e a contagem mentiria.
    private static final String CONSULTA = """
            select l.codigo as codigo,
                   upper(e.nome) as nome,
                   count(p.id) as quantidade_produtores,
                   l.taxa_operacional as valor
            from laboratorio l
            join entidade_agricola e on e.id = l.id
            join produtor p on p.laboratorio_id = l.id
            where (cast(:dataInicialDe as timestamptz) is null or p.data_inicial >= :dataInicialDe)
              and (cast(:dataInicialAte as timestamptz) is null or p.data_inicial <= :dataInicialAte)
              and (cast(:dataFinalDe as timestamptz) is null or p.data_final >= :dataFinalDe)
              and (cast(:dataFinalAte as timestamptz) is null or p.data_final <= :dataFinalAte)
              and (cast(:observacoes as text) is null
                   or p.observacoes ilike concat('%', :observacoes, '%') escape '\\')
            group by l.id, l.codigo, e.nome, l.taxa_operacional
            having count(p.id) >= :quantidadeMinima
            """;

    private static final String ORDEM_POR_CODIGO = " order by count(p.id) desc, l.codigo asc";

    private static final String ORDEM_POR_DATA = " order by count(p.id) desc, min(p.data_inicial) asc, l.codigo asc";

    private final NamedParameterJdbcTemplate jdbc;

    public LaboratorioAnaliticoConsulta(NamedParameterJdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public List<LaboratorioAnaliticoResponse> consultar(
            OffsetDateTime dataInicialDe,
            OffsetDateTime dataInicialAte,
            OffsetDateTime dataFinalDe,
            OffsetDateTime dataFinalAte,
            String observacoes,
            int quantidadeMinima,
            boolean ordenarPelaDataInicial
    ) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        data(params, "dataInicialDe", dataInicialDe);
        data(params, "dataInicialAte", dataInicialAte);
        data(params, "dataFinalDe", dataFinalDe);
        data(params, "dataFinalAte", dataFinalAte);
        params.addValue("observacoes", observacoes, Types.VARCHAR);
        params.addValue("quantidadeMinima", quantidadeMinima);

        String sql = CONSULTA + (ordenarPelaDataInicial ? ORDEM_POR_DATA : ORDEM_POR_CODIGO);
        return jdbc.query(sql, params, (rs, row) -> new LaboratorioAnaliticoResponse(
                rs.getLong("codigo"),
                rs.getString("nome"),
                rs.getLong("quantidade_produtores"),
                rs.getBigDecimal("valor")
        ));
    }

    private void data(MapSqlParameterSource params, String nome, OffsetDateTime valor) {
        params.addValue(nome, valor, Types.TIMESTAMP_WITH_TIMEZONE);
    }
}
