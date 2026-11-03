package br.com.agrotis.painel.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;

import java.math.BigDecimal;

@Entity
@Table(name = "laboratorio")
@PrimaryKeyJoinColumn(name = "id")
public class Laboratorio extends EntidadeAgricola {

    @Column(name = "codigo", nullable = false, unique = true, updatable = false)
    private Long codigo;

    @Column(name = "taxa_operacional", nullable = false, precision = 15, scale = 2)
    private BigDecimal taxaOperacional;

    public Long getCodigo() {
        return codigo;
    }

    public void setCodigo(Long codigo) {
        this.codigo = codigo;
    }

    public BigDecimal getTaxaOperacional() {
        return taxaOperacional;
    }

    public void setTaxaOperacional(BigDecimal taxaOperacional) {
        this.taxaOperacional = taxaOperacional;
    }

    @Override
    public BigDecimal calcularValorFinanceiro() {
        return taxaOperacional;
    }

    @Override
    public String getTipo() {
        return "Laboratorio";
    }
}
