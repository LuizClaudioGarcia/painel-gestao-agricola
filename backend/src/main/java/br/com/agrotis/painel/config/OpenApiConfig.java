package br.com.agrotis.painel.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.Schema;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI painelAgricola() {
        return new OpenAPI().info(new Info()
                .title("Painel de gestão agrícola")
                .description("Cadastro de produtores e listas para os seletores de laboratório e propriedade.")
                .version("1.0"));
    }

    @Bean
    public OpenApiCustomizer uuidComoTexto() {
        return openApi -> {
            if (openApi.getComponents() != null && openApi.getComponents().getSchemas() != null) {
                openApi.getComponents().getSchemas().values().forEach(OpenApiConfig::tirarFormatoUuid);
            }
            if (openApi.getPaths() == null) {
                return;
            }
            openApi.getPaths().values().forEach(caminho ->
                    caminho.readOperations().forEach(operacao -> {
                        if (operacao.getParameters() != null) {
                            operacao.getParameters().forEach(parametro -> tirarFormatoUuid(parametro.getSchema()));
                        }
                    }));
        };
    }

    private static void tirarFormatoUuid(Schema<?> schema) {
        if (schema == null) {
            return;
        }
        if ("uuid".equals(schema.getFormat())) {
            schema.setFormat(null);
            if (schema.getDescription() == null || schema.getDescription().isBlank()) {
                schema.setDescription("UUID");
            }
        }
        if (schema.getProperties() != null) {
            schema.getProperties().values().forEach(OpenApiConfig::tirarFormatoUuid);
        }
        tirarFormatoUuid(schema.getItems());
    }
}
