package br.app.appLogin.dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record MesaDTO(
        Long id,

        @NotNull(message = "O número é obrigatório")
        @Positive(message = "O número deve ser positivo")
        Integer numero,

        @NotNull(message = "A capacidade é obrigatória")
        @Positive(message = "A capacidade deve ser positiva")
        Integer capacidade,

        @NotNull(message = "O status é obrigatório")
        String status
) {
}
