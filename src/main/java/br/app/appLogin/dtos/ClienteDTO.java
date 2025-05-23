package br.app.appLogin.dtos;

import jakarta.validation.constraints.NotBlank;

public record ClienteDTO(
        Long id,

        @NotBlank(message = "O nome é obrigatório")
        String nome
) {
}
