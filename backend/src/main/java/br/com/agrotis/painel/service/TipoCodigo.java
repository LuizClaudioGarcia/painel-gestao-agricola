package br.com.agrotis.painel.service;

public enum TipoCodigo {
    PRODUTOR("produtor_codigo_seq"),
    PROPRIEDADE("propriedade_codigo_seq"),
    LABORATORIO("laboratorio_codigo_seq");

    private final String sequence;

    TipoCodigo(String sequence) {
        this.sequence = sequence;
    }

    public String sequence() {
        return sequence;
    }
}
