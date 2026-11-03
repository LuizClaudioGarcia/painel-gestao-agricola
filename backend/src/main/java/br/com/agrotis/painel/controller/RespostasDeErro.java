package br.com.agrotis.painel.controller;

import br.com.agrotis.painel.dto.ErroResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@ApiResponses({
        @ApiResponse(responseCode = "400", description = "Campo vazio, data final anterior à inicial, UUID fora do formato ou JSON inválido.", content = @Content(schema = @Schema(implementation = ErroResponse.class))),
        @ApiResponse(responseCode = "404", description = "Não existe produtor, laboratório ou propriedade com o UUID informado.", content = @Content(schema = @Schema(implementation = ErroResponse.class))),
        @ApiResponse(responseCode = "409", description = "A inscrição fiscal já está em outro cadastro.", content = @Content(schema = @Schema(implementation = ErroResponse.class)))
})
public @interface RespostasDeErro {
}
