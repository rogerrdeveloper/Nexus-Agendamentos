package dev.rogerrdeveloper.miniagendamentoX.service;

import dev.rogerrdeveloper.miniagendamentoX.dto.AgendamentoCreateRequest;
import dev.rogerrdeveloper.miniagendamentoX.dto.AgendamentoResponse;
import dev.rogerrdeveloper.miniagendamentoX.dto.AgendamentoUpdateRequest;
import dev.rogerrdeveloper.miniagendamentoX.mapper.AgendamentoMapper;
import dev.rogerrdeveloper.miniagendamentoX.model.Agendamento;
import dev.rogerrdeveloper.miniagendamentoX.model.StatusAgendamento;
import dev.rogerrdeveloper.miniagendamentoX.repository.AgendamentoRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AgendamentoService {

    private final AgendamentoRepository repo;

    public AgendamentoService(AgendamentoRepository repo) {
        this.repo = repo;
    }

    @Transactional
    public AgendamentoResponse criar(@Valid AgendamentoCreateRequest req) {
        validarIntervalor(req.dataInicio(), req.dataFim());
        checkConflito(req.usuario(), req.dataInicio(), req.dataFim(), null);

        Agendamento entity = AgendamentoMapper.toEntity(req);
        entity = repo.save(entity);
        return AgendamentoMapper.toResponse(entity);
    }

    @Transactional
    public AgendamentoResponse concluir(Long id) {
        Agendamento entity = repo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Agendamento não encontrado"));
        entity.setStatus(StatusAgendamento.CONCLUIDO);
        entity = repo.save(entity);
        return AgendamentoMapper.toResponse(entity);
    }

    public AgendamentoResponse buscarPorId(@Valid  Long id) {
        Agendamento entity = repo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Agendamento não encontrado"));
        return AgendamentoMapper.toResponse(entity);
    }

    public AgendamentoResponse cancelar(@Valid Long id) {
        Agendamento entity = repo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Agendamento não encontrado"));
        entity.setStatus(StatusAgendamento.CANCELADO);
        entity = repo.save(entity);
        return AgendamentoMapper.toResponse(entity);
    }

    @Transactional
    public AgendamentoResponse atualizar(Long id, @Valid AgendamentoUpdateRequest req) {
        Agendamento entity = repo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Agendamento não encontrado"));
        AgendamentoMapper.merge(entity, req);
        validarIntervalor(req.dataInicio(), req.datafim());
        checkConflito(entity.getUsuario(), req.dataInicio(), req.datafim(), entity.getId());
        entity = repo.save(entity);
        return AgendamentoMapper.toResponse(entity);
    }

    public java.util.List<AgendamentoResponse> listarTodos() {
        return repo.findAll()
                .stream()
                .map(AgendamentoMapper::toResponse)
                .toList();
    }

    public List<AgendamentoResponse> listarPorUsuario(String usuario) {
        return repo.findByUsuarioIgnoreCase(usuario).stream().map(AgendamentoMapper::toResponse).toList();
    }

    private void validarIntervalor(LocalDateTime inicio, LocalDateTime fim) {
        if (inicio == null || fim == null || !inicio.isBefore(fim)) {
            throw new IllegalArgumentException(
                    "Intervalo inválido: data inicio deve ser anterior a data fim"
            );
        }
    }

    private void checkConflito(String usuario, LocalDateTime inicio, LocalDateTime fim, Long id) {
        if (repo.existsConflit(usuario, inicio, fim, id)) {
            throw new IllegalArgumentException("Conflito na agenda: já existe um agendamento nesse periodo");
        }
    }
}
