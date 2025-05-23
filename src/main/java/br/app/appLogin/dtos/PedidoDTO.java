package br.app.appLogin.dtos;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PedidoDTO(
        Long id,

        @NotNull(message = "A data é obrigatória")
        LocalDateTime data,

        @NotNull(message = "O status é obrigatório")
        String status,

        @NotNull(message = "O total é obrigatório")
        BigDecimal total,

        @NotNull(message = "A mesa é obrigatória")
        Long mesaId,

        Long clienteId,

        @NotNull(message = "O usuário é obrigatório")
        Long usuarioId
) {
}
