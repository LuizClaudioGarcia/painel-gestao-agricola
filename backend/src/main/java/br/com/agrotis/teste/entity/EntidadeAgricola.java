package br.com.agrotis.teste.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

import br.com.agrotis.teste.identificador.UuidV7;

import java.util.UUID;

@Entity
@Table(name = "entidade_agricola")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class EntidadeAgricola {

    @Id
    private UUID id;

    @Column(name = "nome", nullable = false, length = 150)
    private String nome;

    @Column(name = "inscricao_fiscal", nullable = false, unique = true, length = 32)
    private String inscricaoFiscal;

    @Column(name = "endereco", nullable = false, length = 255)
    private String endereco;

    protected EntidadeAgricola() {
    }

    @PrePersist
    void gerarId() {
        if (id == null) {
            id = UuidV7.gerar();
        }
    }

    public UUID getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getInscricaoFiscal() {
        return inscricaoFiscal;
    }

    public void setInscricaoFiscal(String inscricaoFiscal) {
        this.inscricaoFiscal = inscricaoFiscal;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }
}
