package br.com.agrotis.painel.exception;

public class InscricaoDuplicadaException extends RuntimeException {

    public static final String MENSAGEM = "Inscrição fiscal já cadastrada";

    public InscricaoDuplicadaException() {
        super(MENSAGEM);
    }
}
