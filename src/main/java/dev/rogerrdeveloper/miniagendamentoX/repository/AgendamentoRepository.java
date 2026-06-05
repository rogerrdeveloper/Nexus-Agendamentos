package dev.rogerrdeveloper.miniagendamentoX.repository;

import dev.rogerrdeveloper.miniagendamentoX.model.Agendamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface AgendamentoRepository extends JpaRepository<Agendamento, Long> {

    // 1. Método automático para listar agendamentos filtrados por usuário (usado no carregarAgendamentos)
    List<Agendamento> findByUsuarioIgnoreCase(String usuario);

    // 2. Método de validação antiqueda de conflitos com a Query posicionada corretamente
    @Query("""
        SELECT CASE WHEN COUNT(a) > 0 THEN true ELSE false END
        FROM Agendamento a
        WHERE a.usuario = :usuario
          AND a.status = dev.rogerrdeveloper.miniagendamentoX.model.StatusAgendamento.AGENDADO
          AND (a.dataInicio < :fim AND a.dataFim > :inicio)
          AND (:ignoreID IS NULL OR a.id <> :ignoreID)
    """)
    boolean existsConflit(@Param("usuario") String usuario,
                          @Param("inicio") LocalDateTime inicio,
                          @Param("fim") LocalDateTime fim,
                          @Param("ignoreID") Long ignoreID);
}