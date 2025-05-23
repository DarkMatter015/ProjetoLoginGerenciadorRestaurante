package br.app.appLogin.dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record ItemPedidoDTO(
        Long id,

        @NotNull(message = "A quantidade é obrigatória")
        @Positive(message = "A quantidade deve ser positiva")
        Integer quantidade,

        @NotNull(message = "O preço é obrigatório")
        @Positive(message = "O preço deve ser positivo")
        BigDecimal preco,

        @NotNull(message = "O pedido é obrigatório")
        Long pedidoId,

        @NotNull(message = "O produto é obrigatório")
        Long produtoId
) {
}
