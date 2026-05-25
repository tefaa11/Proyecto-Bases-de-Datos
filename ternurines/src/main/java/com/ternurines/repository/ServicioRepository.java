package com.ternurines.repository;


import com.ternurines.model.Servicio;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class ServicioRepository {

    private final JdbcTemplate jdbcTemplate;

    public ServicioRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Servicio> servicioMapper =
        (rs, rowNum) -> {
            Servicio servicio =
                new Servicio();

            servicio.setIdServicio(
                rs.getInt("id_servicio"));

            servicio.setNombre(
                rs.getString("nombre"));

            servicio.setDescripcion(
                rs.getString("descripcion"));

            servicio.setPrecio(
                rs.getDouble("precio"));

            return servicio;
        };

    public List<Servicio> findAll() {
        String sql =
                "SELECT * FROM servicio";

        return jdbcTemplate.query(
                sql,
                servicioMapper
        );
    }

    public Optional<Servicio> findById(int id) {
        String sql = """
                SELECT *
                FROM servicio
                WHERE id_servicio = ?
                """;
        List<Servicio> servicios =
                jdbcTemplate.query(
                        sql,
                        servicioMapper,
                        id
                );
        return servicios
                .stream()
                .findFirst();
    }

    // Busca todos los servicios de una cita (JOIN con cita_servicio)
    public List<Servicio> findByCita(int idCita) {
        String sql = """
                SELECT s.*
                FROM servicio s
                JOIN cita_servicio cs
                  ON s.id_servicio = cs.id_servicio
                WHERE cs.id_cita = ?
                """;
        return jdbcTemplate.query(
                sql,
                servicioMapper,
                idCita
        );
    }

    public void save(Servicio servicio) {
        String sql = """
                INSERT INTO servicio
                (nombre, descripcion, precio)
                VALUES (?, ?, ?)
                """;
        jdbcTemplate.update(
                sql,
                servicio.getNombre(),
                servicio.getDescripcion(),
                servicio.getPrecio()
        );
    }

    public void update(Servicio servicio) {
        String sql = """
                UPDATE servicio
                SET nombre = ?,
                    descripcion = ?,
                    precio = ?
                WHERE id_servicio = ?
                """;
        jdbcTemplate.update(
                sql,
                servicio.getNombre(),
                servicio.getDescripcion(),
                servicio.getPrecio(),
                servicio.getIdServicio()
        );
    }

    public void deleteById(int id) {
        String sql = """
                DELETE FROM servicio
                WHERE id_servicio = ?
                """;
        jdbcTemplate.update(sql, id);
    }
}