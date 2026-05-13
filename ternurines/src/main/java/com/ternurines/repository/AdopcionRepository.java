package com.ternurines.repository;

import com.ternurines.model.Adopcion;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

@Repository
public class AdopcionRepository {

    private final JdbcTemplate jdbcTemplate;

    public AdopcionRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private Adopcion mapRow(java.sql.ResultSet rs, int rowNum) throws java.sql.SQLException {
        Adopcion a = new Adopcion();
        a.setIdAdoptante(rs.getInt("id_adoptante"));
        a.setIdMascotaAdopcion(rs.getInt("id_mascota_adopcion"));

        Date fechaAdopcion = rs.getDate("fecha_adopcion");
        if (fechaAdopcion != null) {
            a.setFechaAdopcion(fechaAdopcion.toLocalDate());
        }

        return a;
    }

    public List<Adopcion> findAll() {
        String sql = "SELECT * FROM adopcion ORDER BY id_adoptante, id_mascota_adopcion";
        return jdbcTemplate.query(sql, this::mapRow);
    }

    public Optional<Adopcion> findById(int idAdoptante, int idMascotaAdopcion) {
        String sql = """
                SELECT * FROM adopcion
                WHERE id_adoptante = ? AND id_mascota_adopcion = ?
                """;
        List<Adopcion> resultado = jdbcTemplate.query(sql, this::mapRow, idAdoptante, idMascotaAdopcion);
        return resultado.stream().findFirst();
    }

    public int save(Adopcion adopcion) {
        String sql = """
                INSERT INTO adopcion (id_adoptante, id_mascota_adopcion, fecha_adopcion)
                VALUES (?, ?, ?)
                """;
        return jdbcTemplate.update(
                sql,
                adopcion.getIdAdoptante(),
                adopcion.getIdMascotaAdopcion(),
                adopcion.getFechaAdopcion() != null ? Date.valueOf(adopcion.getFechaAdopcion()) : null
        );
    }

    public int update(Adopcion adopcion) {
        String sql = """
                UPDATE adopcion
                SET fecha_adopcion = ?
                WHERE id_adoptante = ? AND id_mascota_adopcion = ?
                """;
        return jdbcTemplate.update(
                sql,
                adopcion.getFechaAdopcion() != null ? Date.valueOf(adopcion.getFechaAdopcion()) : null,
                adopcion.getIdAdoptante(),
                adopcion.getIdMascotaAdopcion()
        );
    }

    public int delete(int idAdoptante, int idMascotaAdopcion) {
        String sql = """
                DELETE FROM adopcion
                WHERE id_adoptante = ? AND id_mascota_adopcion = ?
                """;
        return jdbcTemplate.update(sql, idAdoptante, idMascotaAdopcion);
    }
}