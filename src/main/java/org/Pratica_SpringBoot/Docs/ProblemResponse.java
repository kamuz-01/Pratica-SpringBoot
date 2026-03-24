package org.Pratica_SpringBoot.Docs;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Erro retornado pela API")
public class ProblemResponse {

    @Schema(example = "404")
    private int status;

    @Schema(example = "Recurso não encontrado")
    private String titulo;

    @Schema(example = "Usuário com ID 10 não encontrado")
    private String detalhe;

    @Schema(example = "/api/v1/usuarios/10")
    private String instancia;

    @Schema(example = "21/03/2026 17:41:50")
    private String timestamp;

    @Schema(example = "GET")
    private String metodo;
}