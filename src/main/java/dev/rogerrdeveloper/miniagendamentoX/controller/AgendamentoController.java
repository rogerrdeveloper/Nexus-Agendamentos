package dev.rogerrdeveloper.miniagendamentoX.controller;

import dev.rogerrdeveloper.miniagendamentoX.dto.AgendamentoCreateRequest;
import dev.rogerrdeveloper.miniagendamentoX.dto.AgendamentoResponse;
import dev.rogerrdeveloper.miniagendamentoX.dto.AgendamentoUpdateRequest;
import dev.rogerrdeveloper.miniagendamentoX.service.AgendamentoService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/agendamentos")
@CrossOrigin(origins = "*")
public class AgendamentoController {
    private final AgendamentoService service;

    public AgendamentoController(AgendamentoService service) {
        this.service = service;
    }

    @PostMapping
    public AgendamentoResponse criar (@Valid @RequestBody AgendamentoCreateRequest request) {
        return service.criar(request);
    }

    @PutMapping("/{id}")
    public AgendamentoResponse atualizar (@PathVariable Long id, @Valid @RequestBody AgendamentoUpdateRequest request) {
        return service.atualizar(id, request);
    }

    @GetMapping
    public List<AgendamentoResponse> listarTodos(@RequestParam(required = false) String usuario) {
        if (usuario != null) {
            return service.listarPorUsuario(usuario);
        }
        return service.listarTodos();
    }

    @PutMapping("/{id}/cancelar")
    public AgendamentoResponse cancelar (@PathVariable Long id) {
        return service.cancelar(id);
    }

    @PutMapping("/{id}/concluir")
    public AgendamentoResponse concluir (@PathVariable Long id) {
        return service.concluir(id);
    }

    @GetMapping("/{id}/buscar")
    public AgendamentoResponse buscarporId(@PathVariable Long id) {
        return service.buscarPorId(id);
    }
}
