package br.com.agrotis.teste.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;

import java.math.BigDecimal;

@Entity
@Table(name = "propriedade")
@PrimaryKeyJoinColumn(name = "id")
public class Propriedade extends EntidadeAgricola {

    @Column(name = "codigo", nullable = false, unique = true, updatable = false)
    private Long codigo;

    @Column(name = "taxa_territorial", nullable = false, precision = 15, scale = 2)
    private BigDecimal taxaTerritorial;

    public Long getCodigo() {
        return codigo;
    }

    public void setCodigo(Long codigo) {
        this.codigo = codigo;
    }

    public BigDecimal getTaxaTerritorial() {
        return taxaTerritorial;
    }

    public void setTaxaTerritorial(BigDecimal taxaTerritorial) {
        this.taxaTerritorial = taxaTerritorial;
    }
}
