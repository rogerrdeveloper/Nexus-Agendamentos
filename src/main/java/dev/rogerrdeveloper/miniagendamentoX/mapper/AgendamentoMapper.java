package dev.rogerrdeveloper.miniagendamentoX.mapper;

import dev.rogerrdeveloper.miniagendamentoX.dto.AgendamentoCreateRequest;
import dev.rogerrdeveloper.miniagendamentoX.dto.AgendamentoResponse;
import dev.rogerrdeveloper.miniagendamentoX.dto.AgendamentoUpdateRequest;
import dev.rogerrdeveloper.miniagendamentoX.model.Agendamento;
import dev.rogerrdeveloper.miniagendamentoX.model.StatusAgendamento;

import java.time.LocalDateTime;


public class AgendamentoMapper {

    public static Agendamento toEntity(AgendamentoCreateRequest req) {
        return Agendamento.builder()
                .titulo(req.titulo())
                .descricao(req.descricao())
                .dataInicio(req.dataInicio())
                .dataFim(req.dataFim())
                .status(StatusAgendamento.AGENDADO)
                .usuario(req.usuario())
                .criadoEm(LocalDateTime.now())
                .atualizadoEm(LocalDateTime.now())
                .build();
    }

    public static void merge(Agendamento entity, AgendamentoUpdateRequest req) {
        if (req.titulo() != null) {
            entity.setTitulo(req.titulo());
        }

        if (req.descricao() != null) {
            entity.setDescricao(req.descricao());
        }

        if (req.dataInicio() != null) {
            entity.setDataInicio(req.dataInicio());
        }

        if (req.datafim() != null) {
            entity.setDataFim(req.datafim());
        }
    }

    public static AgendamentoResponse toResponse(Agendamento a) {
        return new AgendamentoResponse(
                a.getId(),
                a.getTitulo(),
                a.getDescricao(),
                a.getDataInicio(),
                a.getDataFim(),
                a.getStatus(),
                a.getUsuario(),
                a.getCriadoEm(),
                a.getAtualizadoEm()
        );

    }
}
