package br.com.agrotis.teste.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "produtor")
@PrimaryKeyJoinColumn(name = "id")
public class Produtor extends EntidadeAgricola {

    @Column(name = "codigo", nullable = false, unique = true, updatable = false)
    private Long codigo;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "laboratorio_id", nullable = false)
    private Laboratorio laboratorio;

    @ManyToMany
    @JoinTable(
            name = "produtor_propriedade",
            joinColumns = @JoinColumn(name = "produtor_id"),
            inverseJoinColumns = @JoinColumn(name = "propriedade_id")
    )
    private Set<Propriedade> propriedades = new HashSet<>();

    @Column(name = "data_inicial", nullable = false)
    private OffsetDateTime dataInicial;

    @Column(name = "data_final", nullable = false)
    private OffsetDateTime dataFinal;

    @Column(name = "observacoes", columnDefinition = "text")
    private String observacoes;

    @Column(name = "comissao_producao", nullable = false, precision = 15, scale = 2)
    private BigDecimal comissaoProducao;

    public Long getCodigo() {
        return codigo;
    }

    public void setCodigo(Long codigo) {
        this.codigo = codigo;
    }

    public Laboratorio getLaboratorio() {
        return laboratorio;
    }

    public void setLaboratorio(Laboratorio laboratorio) {
        this.laboratorio = laboratorio;
    }

    public Set<Propriedade> getPropriedades() {
        return propriedades;
    }

    public void setPropriedades(Set<Propriedade> propriedades) {
        this.propriedades = propriedades;
    }

    public OffsetDateTime getDataInicial() {
        return dataInicial;
    }

    public void setDataInicial(OffsetDateTime dataInicial) {
        this.dataInicial = dataInicial;
    }

    public OffsetDateTime getDataFinal() {
        return dataFinal;
    }

    public void setDataFinal(OffsetDateTime dataFinal) {
        this.dataFinal = dataFinal;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

    public BigDecimal getComissaoProducao() {
        return comissaoProducao;
    }

    public void setComissaoProducao(BigDecimal comissaoProducao) {
        this.comissaoProducao = comissaoProducao;
    }
}
