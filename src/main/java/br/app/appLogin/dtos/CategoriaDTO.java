package br.app.appLogin.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CategoriaDTO(
        Long id,

        @NotBlank(message = "O nome é obrigatório")
        @Size(max = 100, message = "O nome deve ter até 100 caracteres")
        String nome,

        long produtoCount
) {
}
