package com.ternurines.repository;

import com.ternurines.model.VistaAgendaDiaria;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Time;
import java.util.List;

@Repository
public class VistaAgendaDiariaRepository {

    private final JdbcTemplate jdbcTemplate;

    public VistaAgendaDiariaRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<VistaAgendaDiaria> findAll() {
        String sql = """
            SELECT *
            FROM vista_agenda_diaria
            ORDER BY fecha, hora
            """;

        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            VistaAgendaDiaria v = new VistaAgendaDiaria();
            v.setIdCita(rs.getInt("id_cita"));
            if (rs.getDate("fecha") != null) {
                v.setFecha(rs.getDate("fecha").toLocalDate());
            }
            Time hora = rs.getTime("hora");
            if (hora != null) {
                v.setHora(hora.toLocalTime());
            }
            v.setVeterinario(rs.getString("veterinario"));
            v.setMascota(rs.getString("mascota"));
            v.setCliente(rs.getString("cliente"));
            v.setRecepcionista(rs.getString("recepcionista"));
            v.setMotivo(rs.getString("motivo"));
            v.setEstado(rs.getString("estado"));
            return v;
        });
    }
}