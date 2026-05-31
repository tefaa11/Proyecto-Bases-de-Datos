package com.ternurines.repository;


import com.ternurines.model.Cita;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class CitaRepository {

    private final JdbcTemplate jdbcTemplate;

    public CitaRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Cita> citaMapper =
        (rs, rowNum) -> {
            Cita cita = new Cita();

            cita.setIdCita(
                rs.getInt("id_cita"));

            cita.setIdMascota(
                rs.getInt("id_mascota"));

            cita.setIdVeterinario(
                rs.getInt("id_veterinario"));

            cita.setIdRecepcionista(
                rs.getInt("id_recepcionista"));

            cita.setFecha(
                rs.getDate("fecha").toLocalDate());

            cita.setHora(
                rs.getTime("hora").toLocalTime());

            cita.setMotivo(
                rs.getString("motivo"));

            cita.setEstado(
                rs.getString("estado"));

            return cita;
        };

    public List<Cita> findAll() {
        String sql =
                "SELECT * FROM cita";

        return jdbcTemplate.query(
                sql,
                citaMapper
        );
    }

    public Optional<Cita> findById(int id) {
        String sql = """
                SELECT *
                FROM cita
                WHERE id_cita = ?
                """;
        List<Cita> citas =
                jdbcTemplate.query(
                        sql,
                        citaMapper,
                        id
                );
        return citas
                .stream()
                .findFirst();
    }

    public List<Cita> findByMascota(int idMascota) {
        String sql = """
                SELECT *
                FROM cita
                WHERE id_mascota = ?
                """;
        return jdbcTemplate.query(
                sql,
                citaMapper,
                idMascota
        );
    }

    public List<Cita> findByVeterinario(int idVeterinario) {
        String sql = """
                SELECT *
                FROM cita
                WHERE id_veterinario = ?
                """;
        return jdbcTemplate.query(
                sql,
                citaMapper,
                idVeterinario
        );
    }

    public void save(Cita cita) {
        String sql = """
                INSERT INTO cita
                (id_mascota, id_veterinario,
                 id_recepcionista, fecha,
                 hora, motivo, estado)
                VALUES (?, ?, ?, ?, ?, ?, ?)
                """;
        jdbcTemplate.update(
                sql,
                cita.getIdMascota(),
                cita.getIdVeterinario(),
                cita.getIdRecepcionista(),
                cita.getFecha(),
                cita.getHora(),
                cita.getMotivo(),
                cita.getEstado()
        );
    }

    public Cita saveAndReturn(Cita cita) {
        String sql = """
                INSERT INTO cita
                (id_mascota, id_veterinario,
                 id_recepcionista, fecha,
                 hora, motivo, estado)
                VALUES (?, ?, ?, ?, ?, ?, ?)
                RETURNING id_cita
                """;

        Integer idCita = jdbcTemplate.queryForObject(
                sql,
                Integer.class,
                cita.getIdMascota(),
                cita.getIdVeterinario(),
                cita.getIdRecepcionista(),
                cita.getFecha(),
                cita.getHora(),
                cita.getMotivo(),
                cita.getEstado()
        );

        if (idCita != null) {
            cita.setIdCita(idCita);
        }
        return cita;
    }

    public void update(Cita cita) {
        String sql = """
                UPDATE cita
                SET id_mascota = ?,
                    id_veterinario = ?,
                    id_recepcionista = ?,
                    fecha = ?,
                    hora = ?,
                    motivo = ?,
                    estado = ?
                WHERE id_cita = ?
                """;
        jdbcTemplate.update(
                sql,
                cita.getIdMascota(),
                cita.getIdVeterinario(),
                cita.getIdRecepcionista(),
                cita.getFecha(),
                cita.getHora(),
                cita.getMotivo(),
                cita.getEstado(),
                cita.getIdCita()
        );
    }

    public void deleteById(int id) {
        String sql = """
                DELETE FROM cita
                WHERE id_cita = ?
                """;
        jdbcTemplate.update(sql, id);
    }
}
