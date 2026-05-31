package com.ternurines.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.ternurines.model.Tratamiento;

@Repository
public class TratamientoRepository {

    private final JdbcTemplate jdbcTemplate;

    public TratamientoRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Tratamiento> tratamientoMapper =
        (rs, rowNum) -> {
            Tratamiento tratamiento = new Tratamiento();
            tratamiento.setIdTratamiento(rs.getInt("id_tratamiento"));
            tratamiento.setIdHistorial(rs.getInt("id_historial"));
            tratamiento.setIdMedicamento(rs.getInt("id_medicamento"));
            tratamiento.setDescripcion(rs.getString("descripcion"));
            tratamiento.setDosis(rs.getString("dosis"));
            tratamiento.setFechaInicio(rs.getDate("fecha_inicio").toLocalDate());
            // fecha_fin es nullable (chk_trat_fechas: NULL o >= fecha_inicio)
            java.sql.Date fechaFin = rs.getDate("fecha_fin");
            tratamiento.setFechaFin(fechaFin == null ? null : fechaFin.toLocalDate());
            return tratamiento;
        };

    // rol_veterinario tiene SELECT e INSERT en historial_medico;
    // tratamiento está relacionado y solo admin tiene ALL PRIVILEGES
    public List<Tratamiento> findAll() {
        String sql = "SELECT * FROM tratamiento";
        return jdbcTemplate.query(sql, tratamientoMapper);
    }

    public Optional<Tratamiento> findById(int id) {
        String sql = """
                SELECT *
                FROM tratamiento
                WHERE id_tratamiento = ?
                """;
        List<Tratamiento> result = jdbcTemplate.query(sql, tratamientoMapper, id);
        return result.stream().findFirst();
    }

    // Todos los tratamientos asociados a un historial médico concreto
    public List<Tratamiento> findByIdHistorial(int idHistorial) {
        String sql = """
                SELECT *
                FROM tratamiento
                WHERE id_historial = ?
                ORDER BY fecha_inicio
                """;
        return jdbcTemplate.query(sql, tratamientoMapper, idHistorial);
    }

    // Tratamientos activos (sin fecha de fin o con fecha fin futura)
    public List<Tratamiento> findActivos() {
        String sql = """
                SELECT *
                FROM tratamiento
                WHERE fecha_fin IS NULL
                   OR fecha_fin >= CURRENT_DATE
                ORDER BY fecha_inicio
                """;
        return jdbcTemplate.query(sql, tratamientoMapper);
    }

    // INSERT: respeta chk_trat_dosis (>= 2 chars),
    //         chk_trat_fecha_inicio (>= 2000-01-01),
    //         chk_trat_fechas (fecha_fin NULL o >= fecha_inicio),
    //         fk_tratamiento_historial (id_historial debe existir),
    //         fk_tratamiento_medicamento (id_medicamento debe existir,
    //                                     ON DELETE RESTRICT)
    public void save(Tratamiento tratamiento) {
        String sql = """
                INSERT INTO tratamiento
                (id_historial, id_medicamento,
                 descripcion, dosis,
                 fecha_inicio, fecha_fin)
                VALUES (?, ?, ?, ?, ?, ?)
                """;
        jdbcTemplate.update(
                sql,
                tratamiento.getIdHistorial(),
                tratamiento.getIdMedicamento(),
                tratamiento.getDescripcion(),
                tratamiento.getDosis(),
                tratamiento.getFechaInicio(),
                tratamiento.getFechaFin()       // puede ser null
        );
    }

    public void update(Tratamiento tratamiento) {
        String sql = """
                UPDATE tratamiento
                SET id_medicamento = ?,
                    descripcion    = ?,
                    dosis          = ?,
                    fecha_inicio   = ?,
                    fecha_fin      = ?
                WHERE id_tratamiento = ?
                """;
        jdbcTemplate.update(
                sql,
                tratamiento.getIdMedicamento(),
                tratamiento.getDescripcion(),
                tratamiento.getDosis(),
                tratamiento.getFechaInicio(),
                tratamiento.getFechaFin(),      // puede ser null
                tratamiento.getIdTratamiento()
        );
    }

    // DELETE en cascada desde historial_medico
    // (fk_tratamiento_historial ON DELETE CASCADE),
    // pero también se puede borrar directamente
    public void deleteById(int id) {
        String sql = """
                DELETE FROM tratamiento
                WHERE id_tratamiento = ?
                """;
        jdbcTemplate.update(sql, id);
    }
}
