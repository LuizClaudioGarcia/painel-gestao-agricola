package br.com.agrotis.teste.identificador;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UuidV7Test {

    @Test
    void geraUuidVersao7() {
        UUID id = UuidV7.gerar();

        assertEquals(7, id.version()); // valida versão 7 do UUID
        assertEquals(2, id.variant()); // valida o formato padrão do UUID
    }
}
