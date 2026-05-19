package com.ternurines.repository;

import com.ternurines.model.Administrador;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class AdministradorRepository {

    private final JdbcTemplate jdbcTemplate;

    public AdministradorRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Administrador> adminMapper =
        (rs, rowNum) -> {
            Administrador admin =
                new Administrador();

            admin.setIdAdministrador(
                rs.getInt("id_administrador"));

            admin.setNombre(
                rs.getString("nombre"));

            admin.setDocumento(
                rs.getString("documento"));

            admin.setTelefono(
                rs.getString("telefono"));

            admin.setCorreo(
                rs.getString("correo"));

            admin.setContrasenia(
                rs.getString("contrasena"));

        return admin;
    };

    public List<Administrador> findAll() {

        String sql =
                "SELECT * FROM administrador";

        return jdbcTemplate.query(
                sql,
                adminMapper
        );
    }

    public Optional<Administrador> findById(int id) {
        String sql = """
                SELECT *
                FROM administrador
                WHERE id_administrador = ?
                """;
        List<Administrador> admins =
                jdbcTemplate.query(
                        sql,
                        adminMapper,
                        id
                );
        return admins
                .stream()
                .findFirst();
    }

    public void save(Administrador admin) {
        String sql = """
                INSERT INTO administrador
                (nombre, documento,
                 telefono, correo,
                 contrasena)
                VALUES (?, ?, ?, ?, ?)
                """;
        jdbcTemplate.update(
                sql,
                admin.getNombre(),
                admin.getDocumento(),
                admin.getTelefono(),
                admin.getCorreo(),
                admin.getContrasenia()
        );
    }

    public void update(Administrador admin) {
        String sql = """
                UPDATE administrador
                SET nombre = ?,
                    documento = ?,
                    telefono = ?,
                    correo = ?,
                    contrasena = ?
                WHERE id_administrador = ?
                """;
        jdbcTemplate.update(
                sql,
                admin.getNombre(),
                admin.getDocumento(),
                admin.getTelefono(),
                admin.getCorreo(),
                admin.getContrasenia(),
                admin.getIdAdministrador()
        );
    }

    public void deleteById(int id) {
        String sql = """
                DELETE FROM administrador
                WHERE id_administrador = ?
                """;
        jdbcTemplate.update(sql, id);
    }
}
