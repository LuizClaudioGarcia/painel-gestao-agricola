package br.com.agrotis.teste.identificador;

import com.github.f4b6a3.uuid.UuidCreator;

import java.util.UUID;

public final class UuidV7 {

    private UuidV7() {
    }

    public static UUID gerar() {
        return UuidCreator.getTimeOrderedEpoch();
    }
}
