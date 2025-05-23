package br.app.appLogin.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record ProdutoDTO(
        Long id,

        @NotBlank(message = "O nome é obrigatório")
        String nome,

        @NotNull(message = "O preço é obrigatório")
        @Positive(message = "O preço deve ser positivo")
        BigDecimal preco,

        @NotNull(message = "A disponibilidade é obrigatória")
        Boolean disponivel,

        @NotNull(message = "A categoria é obrigatória")
        Long categoriaId
) {
}
