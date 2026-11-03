package br.com.agrotis.painel.dto;

import br.com.agrotis.painel.entity.Laboratorio;
import br.com.agrotis.painel.entity.Propriedade;

import java.util.UUID;

public record CadastroResumo(
        UUID id,
        Long codigo,
        String nome,
        String inscricaoFiscal,
        String endereco
) {

    public static CadastroResumo de(Laboratorio laboratorio) {
        return new CadastroResumo(
                laboratorio.getId(),
                laboratorio.getCodigo(),
                laboratorio.getNome(),
                laboratorio.getInscricaoFiscal(),
                laboratorio.getEndereco()
        );
    }

    public static CadastroResumo de(Propriedade propriedade) {
        return new CadastroResumo(
                propriedade.getId(),
                propriedade.getCodigo(),
                propriedade.getNome(),
                propriedade.getInscricaoFiscal(),
                propriedade.getEndereco()
        );
    }
}
