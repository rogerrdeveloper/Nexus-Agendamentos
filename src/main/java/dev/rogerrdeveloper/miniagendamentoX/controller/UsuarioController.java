package dev.rogerrdeveloper.miniagendamentoX.controller;

import dev.rogerrdeveloper.miniagendamentoX.model.Usuario;
import dev.rogerrdeveloper.miniagendamentoX.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/usuarios")
@CrossOrigin(origins = "*")
public class UsuarioController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @PostMapping("/cadastrar")
    public ResponseEntity<?> cadastrar(@RequestBody Usuario usuario) {
        String usernameTratado = usuario.getUsername().trim().toLowerCase();

        if (usuarioRepository.existsByUsernameIgnoreCase(usernameTratado)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Erro: Este nome de usuário já está em uso por outra pessoa.");
        }

        usuario.setUsername(usernameTratado);
        usuarioRepository.save(usuario);
        return ResponseEntity.ok("Usuário registrado com sucesso no Nexus!");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Usuario usuario) {
        Optional<Usuario> usuarioBanco = usuarioRepository.findByUsernameIgnoreCase(usuario.getUsername().trim());

        if (usuarioBanco.isPresent() && usuarioBanco.get().getPassword().equals(usuario.getPassword())) {
            return ResponseEntity.ok("Autenticado");
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuário ou senha incorretos.");
    }
}
