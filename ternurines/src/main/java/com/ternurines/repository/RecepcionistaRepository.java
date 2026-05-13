package com.ternurines.repository;

import com.ternurines.model.Recepcionista;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class RecepcionistaRepository {

    private final JdbcTemplate jdbcTemplate;

    public RecepcionistaRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private Recepcionista mapRow(java.sql.ResultSet rs, int rowNum) throws java.sql.SQLException {
        Recepcionista r = new Recepcionista();
        r.setIdRecepcionista(rs.getInt("id_recepcionista"));
        r.setNombre(rs.getString("nombre"));
        r.setDocumento(rs.getString("documento"));
        r.setTelefono(rs.getString("telefono"));
        r.setCorreo(rs.getString("correo"));
        r.setContrasena(rs.getString("contrasena"));
        return r;
    }

    public List<Recepcionista> findAll() {
        String sql = "SELECT * FROM recepcionista ORDER BY id_recepcionista";
        return jdbcTemplate.query(sql, this::mapRow);
    }

    public Optional<Recepcionista> findById(int idRecepcionista) {
        String sql = "SELECT * FROM recepcionista WHERE id_recepcionista = ?";
        List<Recepcionista> resultado = jdbcTemplate.query(sql, this::mapRow, idRecepcionista);
        return resultado.stream().findFirst();
    }

    public int save(Recepcionista recepcionista) {
        String sql = """
                INSERT INTO recepcionista (nombre, documento, telefono, correo, contrasena)
                VALUES (?, ?, ?, ?, ?)
                """;
        return jdbcTemplate.update(
                sql,
                recepcionista.getNombre(),
                recepcionista.getDocumento(),
                recepcionista.getTelefono(),
                recepcionista.getCorreo(),
                recepcionista.getContrasena()
        );
    }

    public int update(Recepcionista recepcionista) {
        String sql = """
                UPDATE recepcionista
                SET nombre = ?, documento = ?, telefono = ?, correo = ?, contrasena = ?
                WHERE id_recepcionista = ?
                """;
        return jdbcTemplate.update(
                sql,
                recepcionista.getNombre(),
                recepcionista.getDocumento(),
                recepcionista.getTelefono(),
                recepcionista.getCorreo(),
                recepcionista.getContrasena(),
                recepcionista.getIdRecepcionista()
        );
    }

    public int delete(int idRecepcionista) {
        String sql = "DELETE FROM recepcionista WHERE id_recepcionista = ?";
        return jdbcTemplate.update(sql, idRecepcionista);
    }
}