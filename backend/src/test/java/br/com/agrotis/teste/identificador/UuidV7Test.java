package br.com.agrotis.teste.identificador;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UuidV7Test {

    @Test
    void geraUuidVersao7() {
        UUID id = UuidV7.gerar();

        assertEquals(7, id.version()); // se a lib cair no método errado, a versão deixa de ser 7
        assertEquals(2, id.variant()); // variante IETF; sem isso o UUID não é o formato padrão
    }
}
