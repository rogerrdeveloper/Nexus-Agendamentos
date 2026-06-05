package dev.rogerrdeveloper.miniagendamentoX.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false) // ISSO IMPEDE NOMES REPETIDOS NO BANCO!
    private String username;

    @Column(nullable = false)
    private String password;
}