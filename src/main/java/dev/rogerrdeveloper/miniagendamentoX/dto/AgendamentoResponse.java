package dev.rogerrdeveloper.miniagendamentoX.dto;

import dev.rogerrdeveloper.miniagendamentoX.model.StatusAgendamento;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public record AgendamentoResponse(
        Long id,
        @Size(max = 120) String titulo,
        @Size(max = 4000) String descricao,
        LocalDateTime dataInicio,
        LocalDateTime dataFim,
        StatusAgendamento status,
        String usuario,
        LocalDateTime criadoEm,
        LocalDateTime atualizadoEm
) {
}
