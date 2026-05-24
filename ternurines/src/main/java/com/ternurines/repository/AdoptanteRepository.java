package com.ternurines.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.ternurines.model.Adoptante;

@Repository
public class AdoptanteRepository {

    private final JdbcTemplate jdbcTemplate;

    public AdoptanteRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Adoptante> adoptanteMapper =
        (rs, rowNum) -> {
            Adoptante adoptante = new Adoptante();
            adoptante.setIdAdoptante(rs.getInt("id_adoptante"));
            adoptante.setNombre(rs.getString("nombre"));
            adoptante.setDocumento(rs.getString("documento"));
            adoptante.setTelefono(rs.getString("telefono"));
            adoptante.setDireccion(rs.getString("direccion"));
            adoptante.setCorreo(rs.getString("correo"));
            return adoptante;
        };

    // Solo recepcionista y admin tienen acceso a esta tabla
    public List<Adoptante> findAll() {
        String sql = "SELECT * FROM adoptante";
        return jdbcTemplate.query(sql, adoptanteMapper);
    }

    public Optional<Adoptante> findById(int id) {
        String sql = """
                SELECT *
                FROM adoptante
                WHERE id_adoptante = ?
                """;
        List<Adoptante> result = jdbcTemplate.query(sql, adoptanteMapper, id);
        return result.stream().findFirst();
    }

    public Optional<Adoptante> findByDocumento(String documento) {
        String sql = """
                SELECT *
                FROM adoptante
                WHERE documento = ?
                """;
        List<Adoptante> result = jdbcTemplate.query(sql, adoptanteMapper, documento);
        return result.stream().findFirst();
    }

    // INSERT: respeta chk_adoptante_documento (>= 6 chars),
    //         chk_adoptante_nombre (>= 2 chars),
    //         chk_adoptante_correo (formato válido o NULL)
    public void save(Adoptante adoptante) {
        String sql = """
                INSERT INTO adoptante
                (nombre, documento,
                 telefono, direccion, correo)
                VALUES (?, ?, ?, ?, ?)
                """;
        jdbcTemplate.update(
                sql,
                adoptante.getNombre(),
                adoptante.getDocumento(),
                adoptante.getTelefono(),
                adoptante.getDireccion(),
                adoptante.getCorreo()
        );
    }

    public void update(Adoptante adoptante) {
        String sql = """
                UPDATE adoptante
                SET nombre    = ?,
                    documento = ?,
                    telefono  = ?,
                    direccion = ?,
                    correo    = ?
                WHERE id_adoptante = ?
                """;
        jdbcTemplate.update(
                sql,
                adoptante.getNombre(),
                adoptante.getDocumento(),
                adoptante.getTelefono(),
                adoptante.getDireccion(),
                adoptante.getCorreo(),
                adoptante.getIdAdoptante()
        );
    }

    // DELETE restringido: no se puede borrar si tiene adopciones asociadas
    // (fk_adopcion_adoptante ON DELETE RESTRICT)
    public void deleteById(int id) {
        String sql = """
                DELETE FROM adoptante
                WHERE id_adoptante = ?
                """;
        jdbcTemplate.update(sql, id);
    }
}