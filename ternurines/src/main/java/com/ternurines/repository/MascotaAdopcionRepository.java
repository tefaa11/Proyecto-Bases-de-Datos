package com.ternurines.repository;

import com.ternurines.model.MascotaAdopcion;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

@Repository
public class MascotaAdopcionRepository {

    private final JdbcTemplate jdbcTemplate;

    public MascotaAdopcionRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private MascotaAdopcion mapRow(java.sql.ResultSet rs, int rowNum) throws java.sql.SQLException {
        MascotaAdopcion m = new MascotaAdopcion();
        m.setIdMascotaAdopcion(rs.getInt("id_mascota_adopcion"));
        m.setIdRecepcionista(rs.getInt("id_recepcionista"));
        m.setNombre(rs.getString("nombre"));
        m.setEspecie(rs.getString("especie"));
        m.setRaza(rs.getString("raza"));
        m.setEdad(rs.getInt("edad"));
        m.setEstadoSalud(rs.getString("estado_salud"));
        m.setEstadoAdopcion(rs.getString("estado_adopcion"));

        Date fechaIngreso = rs.getDate("fecha_ingreso");
        if (fechaIngreso != null) {
            m.setFechaIngreso(fechaIngreso.toLocalDate());
        }

        return m;
    }

    public List<MascotaAdopcion> findAll() {
        String sql = "SELECT * FROM mascota_adopcion ORDER BY id_mascota_adopcion";
        return jdbcTemplate.query(sql, this::mapRow);
    }

    public Optional<MascotaAdopcion> findById(int idMascotaAdopcion) {
        String sql = "SELECT * FROM mascota_adopcion WHERE id_mascota_adopcion = ?";
        List<MascotaAdopcion> resultado = jdbcTemplate.query(sql, this::mapRow, idMascotaAdopcion);
        return resultado.stream().findFirst();
    }

    public List<MascotaAdopcion> findDisponibles() {
        String sql = """
                SELECT *
                FROM mascota_adopcion
                WHERE estado_adopcion = 'Disponible'
                ORDER BY fecha_ingreso DESC
                """;
        return jdbcTemplate.query(sql, this::mapRow);
    }

    public int save(MascotaAdopcion mascotaAdopcion) {
        String sql = """
                INSERT INTO mascota_adopcion
                (id_recepcionista, nombre, especie, raza, edad, estado_salud, estado_adopcion, fecha_ingreso)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?)
                """;
        return jdbcTemplate.update(
                sql,
                mascotaAdopcion.getIdRecepcionista(),
                mascotaAdopcion.getNombre(),
                mascotaAdopcion.getEspecie(),
                mascotaAdopcion.getRaza(),
                mascotaAdopcion.getEdad(),
                mascotaAdopcion.getEstadoSalud(),
                mascotaAdopcion.getEstadoAdopcion(),
                mascotaAdopcion.getFechaIngreso() != null ? Date.valueOf(mascotaAdopcion.getFechaIngreso()) : null
        );
    }

    public int update(MascotaAdopcion mascotaAdopcion) {
        String sql = """
                UPDATE mascota_adopcion
                SET id_recepcionista = ?, nombre = ?, especie = ?, raza = ?, edad = ?,
                    estado_salud = ?, estado_adopcion = ?, fecha_ingreso = ?
                WHERE id_mascota_adopcion = ?
                """;
        return jdbcTemplate.update(
                sql,
                mascotaAdopcion.getIdRecepcionista(),
                mascotaAdopcion.getNombre(),
                mascotaAdopcion.getEspecie(),
                mascotaAdopcion.getRaza(),
                mascotaAdopcion.getEdad(),
                mascotaAdopcion.getEstadoSalud(),
                mascotaAdopcion.getEstadoAdopcion(),
                mascotaAdopcion.getFechaIngreso() != null ? Date.valueOf(mascotaAdopcion.getFechaIngreso()) : null,
                mascotaAdopcion.getIdMascotaAdopcion()
        );
    }

    public int delete(int idMascotaAdopcion) {
        String sql = "DELETE FROM mascota_adopcion WHERE id_mascota_adopcion = ?";
        return jdbcTemplate.update(sql, idMascotaAdopcion);
    }

    public int actualizarEstado(int idMascotaAdopcion, String estado) {
        String sql = """
                UPDATE mascota_adopcion
                SET estado_adopcion = ?
                WHERE id_mascota_adopcion = ?
                """;
        return jdbcTemplate.update(sql, estado, idMascotaAdopcion);
    }
}
