package br.com.agrotis.painel.service;

import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Component;

@Component
public class GeradorDeCodigo {

    private final EntityManager entityManager;

    public GeradorDeCodigo(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public long proximo(TipoCodigo tipo) {
        // O nome sai do enum. A requisição não escolhe a sequence.
        Object valor = entityManager
                .createNativeQuery("select nextval('" + tipo.sequence() + "')")
                .getSingleResult();
        return ((Number) valor).longValue();
    }
}
