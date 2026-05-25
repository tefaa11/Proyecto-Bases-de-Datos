package com.ternurines.repository;

import com.ternurines.model.Veterinario;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class VeterinarioRepository {

    private final JdbcTemplate jdbcTemplate;

    public VeterinarioRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Veterinario> veterinarioMapper =
        (rs, rowNum) -> {
            Veterinario veterinario =
                new Veterinario();

            veterinario.setIdVeterinario(
                rs.getInt("id_veterinario"));

            veterinario.setNombre(
                rs.getString("nombre"));

            veterinario.setDocumento(
                rs.getString("documento"));

            veterinario.setTelefono(
                rs.getString("telefono"));

            veterinario.setCorreo(
                rs.getString("correo"));

            veterinario.setEspecialidad(
                rs.getString("especialidad"));

            veterinario.setNumLicencia(
                rs.getString("num_licencia"));

            veterinario.setContrasena(
                rs.getString("contrasena"));

            return veterinario;
        };

    public List<Veterinario> findAll() {
        String sql =
                "SELECT * FROM veterinario";

        return jdbcTemplate.query(
                sql,
                veterinarioMapper
        );
    }

    public Optional<Veterinario> findById(int id) {
        String sql = """
                SELECT *
                FROM veterinario
                WHERE id_veterinario = ?
                """;
        List<Veterinario> veterinarios =
                jdbcTemplate.query(
                        sql,
                        veterinarioMapper,
                        id
                );
        return veterinarios
                .stream()
                .findFirst();
    }

    public Optional<Veterinario> findByCorreo(String correo) {
        String sql = """
                SELECT *
                FROM veterinario
                WHERE correo = ?
                """;
        List<Veterinario> veterinarios =
                jdbcTemplate.query(
                        sql,
                        veterinarioMapper,
                        correo
                );
        return veterinarios
                .stream()
                .findFirst();
    }

    public List<Veterinario> findByEspecialidad(
            String especialidad) {

        String sql = """
                SELECT *
                FROM veterinario
                WHERE especialidad = ?
                """;
        return jdbcTemplate.query(
                sql,
                veterinarioMapper,
                especialidad
        );
    }

    public void save(Veterinario veterinario) {
        String sql = """
                INSERT INTO veterinario
                (nombre, documento, telefono,
                 correo, especialidad,
                 num_licencia, contrasena)
                VALUES (?, ?, ?, ?, ?, ?, ?)
                """;
        jdbcTemplate.update(
                sql,
                veterinario.getNombre(),
                veterinario.getDocumento(),
                veterinario.getTelefono(),
                veterinario.getCorreo(),
                veterinario.getEspecialidad(),
                veterinario.getNumLicencia(),
                veterinario.getContrasena()
        );
    }

    public void update(Veterinario veterinario) {
        String sql = """
                UPDATE veterinario
                SET nombre = ?,
                    documento = ?,
                    telefono = ?,
                    correo = ?,
                    especialidad = ?,
                    num_licencia = ?,
                    contrasena = ?
                WHERE id_veterinario = ?
                """;
        jdbcTemplate.update(
                sql,
                veterinario.getNombre(),
                veterinario.getDocumento(),
                veterinario.getTelefono(),
                veterinario.getCorreo(),
                veterinario.getEspecialidad(),
                veterinario.getNumLicencia(),
                veterinario.getContrasena(),
                veterinario.getIdVeterinario()
        );
    }

    public void deleteById(int id) {
        String sql = """
                DELETE FROM veterinario
                WHERE id_veterinario = ?
                """;
        jdbcTemplate.update(sql, id);
    }
}