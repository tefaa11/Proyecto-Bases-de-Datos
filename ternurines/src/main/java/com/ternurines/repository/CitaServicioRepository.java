package com.ternurines.repository;

import com.ternurines.model.CitaServicio;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CitaServicioRepository {

    private final JdbcTemplate jdbcTemplate;

    public CitaServicioRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<CitaServicio> citaServicioMapper =
        (rs, rowNum) -> {
            CitaServicio citaServicio =
                new CitaServicio();

            citaServicio.setIdCita(
                rs.getInt("id_cita"));

            citaServicio.setIdServicio(
                rs.getInt("id_servicio"));

            return citaServicio;
        };

    public List<CitaServicio> findAll() {
        String sql =
                "SELECT * FROM cita_servicio";

        return jdbcTemplate.query(
                sql,
                citaServicioMapper
        );
    }

    // Busca todos los servicios asociados a una cita
    public List<CitaServicio> findByIdCita(int idCita) {
        String sql = """
                SELECT *
                FROM cita_servicio
                WHERE id_cita = ?
                """;
        return jdbcTemplate.query(
                sql,
                citaServicioMapper,
                idCita
        );
    }

    // Busca todas las citas que tienen un servicio dado
    public List<CitaServicio> findByIdServicio(int idServicio) {
        String sql = """
                SELECT *
                FROM cita_servicio
                WHERE id_servicio = ?
                """;
        return jdbcTemplate.query(
                sql,
                citaServicioMapper,
                idServicio
        );
    }

    public void save(CitaServicio citaServicio) {
        String sql = """
                INSERT INTO cita_servicio
                (id_cita, id_servicio)
                VALUES (?, ?)
                """;
        jdbcTemplate.update(
                sql,
                citaServicio.getIdCita(),
                citaServicio.getIdServicio()
        );
    }

    // Elimina la relación entre una cita y un servicio específico
    public void deleteByIdCitaAndIdServicio(
            int idCita, int idServicio) {

        String sql = """
                DELETE FROM cita_servicio
                WHERE id_cita = ?
                AND id_servicio = ?
                """;
        jdbcTemplate.update(sql, idCita, idServicio);
    }

    // Elimina todos los servicios de una cita
    public void deleteByIdCita(int idCita) {
        String sql = """
                DELETE FROM cita_servicio
                WHERE id_cita = ?
                """;
        jdbcTemplate.update(sql, idCita);
    }
}
