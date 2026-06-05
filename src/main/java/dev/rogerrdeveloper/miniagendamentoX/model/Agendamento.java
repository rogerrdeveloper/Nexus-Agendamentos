package dev.rogerrdeveloper.miniagendamentoX.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.stream.DoubleStream;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "tb_agendamento")
/*
  id SERIAL PRIMARY KEY,
    titulo VARCHAR(120) NOT NULL,
    descricao TEXT,
    data_inicio TIMESTAMP NOT NULL,
    data_fim TIMESTAMP NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'AGENDADO',
    usuario VARCHAR(80) NOT NULL,
    criado em TIMESTAMP NOT NULL DEFAULT NOW(),
    atualizado em TIMESTAMP NOT FULL DEFAULT NOW(),
    CONSTRAINT ck_status CHECK (statuss IN ('AGENDADO', 'CANCELADO', 'CONCLUIDO')),
    CONSTRAINT ck_intervalor CHECK (data_inicio < data_fim
 */
public class Agendamento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, length = 120)
    private String titulo;

    @Column(nullable = false, length = 250)
    private String descricao;

    @Column(name = "data_inicio", nullable = false)
    private LocalDateTime dataInicio;

    @Column(name = "data_fim", nullable = false)
    private LocalDateTime dataFim;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StatusAgendamento status;

    @Column(nullable = false, length = 80)
    private String usuario;

    @Column(name = "criado_em", nullable = false)
    private LocalDateTime criadoEm;

    @Column(name = "atualizado_em", nullable = false)
    private LocalDateTime atualizadoEm;

}
