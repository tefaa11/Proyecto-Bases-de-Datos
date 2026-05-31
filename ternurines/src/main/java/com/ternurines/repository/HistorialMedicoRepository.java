package com.ternurines.repository;

import com.ternurines.model.HistorialMedico;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

@Repository
public class HistorialMedicoRepository {

    private final JdbcTemplate jdbcTemplate;

    public HistorialMedicoRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private HistorialMedico mapRow(java.sql.ResultSet rs, int rowNum) throws java.sql.SQLException {
        HistorialMedico h = new HistorialMedico();
        h.setIdHistorial(rs.getInt("id_historial"));
        h.setIdMascota(rs.getInt("id_mascota"));
        h.setIdVeterinario(rs.getInt("id_veterinario"));

        Date fecha = rs.getDate("fecha");
        if (fecha != null) {
            h.setFecha(fecha.toLocalDate());
        }

        h.setDiagnostico(rs.getString("diagnostico"));
        h.setObservaciones(rs.getString("observaciones"));
        return h;
    }

    public List<HistorialMedico> findAll() {
        String sql = "SELECT * FROM historial_medico ORDER BY id_historial";
        return jdbcTemplate.query(sql, this::mapRow);
    }

    public Optional<HistorialMedico> findById(int idHistorial) {
        String sql = "SELECT * FROM historial_medico WHERE id_historial = ?";
        List<HistorialMedico> resultado = jdbcTemplate.query(sql, this::mapRow, idHistorial);
        return resultado.stream().findFirst();
    }

    public int save(HistorialMedico historialMedico) {
        String sql = """
                INSERT INTO historial_medico (id_mascota, id_veterinario, fecha, diagnostico, observaciones)
                VALUES (?, ?, ?, ?, ?)
                """;
        return jdbcTemplate.update(
                sql,
                historialMedico.getIdMascota(),
                historialMedico.getIdVeterinario(),
                historialMedico.getFecha() != null ? Date.valueOf(historialMedico.getFecha()) : null,
                historialMedico.getDiagnostico(),
                historialMedico.getObservaciones()
        );
    }

    public HistorialMedico saveAndReturn(HistorialMedico historialMedico) {
        String sql = """
                INSERT INTO historial_medico (id_mascota, id_veterinario, fecha, diagnostico, observaciones)
                VALUES (?, ?, ?, ?, ?)
                RETURNING id_historial
                """;

        Integer idHistorial = jdbcTemplate.queryForObject(
                sql,
                Integer.class,
                historialMedico.getIdMascota(),
                historialMedico.getIdVeterinario(),
                historialMedico.getFecha() != null ? Date.valueOf(historialMedico.getFecha()) : null,
                historialMedico.getDiagnostico(),
                historialMedico.getObservaciones()
        );

        if (idHistorial != null) {
            historialMedico.setIdHistorial(idHistorial);
        }
        return historialMedico;
    }

    public int update(HistorialMedico historialMedico) {
        String sql = """
                UPDATE historial_medico
                SET id_mascota = ?, id_veterinario = ?, fecha = ?, diagnostico = ?, observaciones = ?
                WHERE id_historial = ?
                """;
        return jdbcTemplate.update(
                sql,
                historialMedico.getIdMascota(),
                historialMedico.getIdVeterinario(),
                historialMedico.getFecha() != null ? Date.valueOf(historialMedico.getFecha()) : null,
                historialMedico.getDiagnostico(),
                historialMedico.getObservaciones(),
                historialMedico.getIdHistorial()
        );
    }

    public int delete(int idHistorial) {
        String sql = "DELETE FROM historial_medico WHERE id_historial = ?";
        return jdbcTemplate.update(sql, idHistorial);
    }
}
