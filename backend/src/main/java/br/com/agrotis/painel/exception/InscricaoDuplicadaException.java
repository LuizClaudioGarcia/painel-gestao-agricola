package br.com.agrotis.painel.exception;

public class InscricaoDuplicadaException extends RuntimeException {

    public static final String MENSAGEM = "Já existe um cadastro com essa inscrição fiscal. Produtor, fazenda e laboratório não repetem o mesmo número.";

    public InscricaoDuplicadaException() {
        super(MENSAGEM);
    }
}
