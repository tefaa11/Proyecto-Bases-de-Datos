package com.ternurines.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.ternurines.model.Medicamento;

@Repository
public class MedicamentoRepository {

    private final JdbcTemplate jdbcTemplate;

    public MedicamentoRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Medicamento> medicamentoMapper =
        (rs, rowNum) -> {
            Medicamento medicamento = new Medicamento();
            medicamento.setIdMedicamento(rs.getInt("id_medicamento"));
            medicamento.setIdAdministrador(rs.getInt("id_administrador"));
            medicamento.setNombre(rs.getString("nombre"));
            medicamento.setDescripcion(rs.getString("descripcion"));
            medicamento.setPrecio(rs.getDouble("precio"));
            medicamento.setStock(rs.getInt("stock"));
            medicamento.setFechaVencimiento(rs.getDate("fecha_vencimiento"));
            return medicamento;
        };

    // Solo rol_admin_general tiene ALL PRIVILEGES sobre esta tabla
    public List<Medicamento> findAll() {
        String sql = "SELECT * FROM medicamento";
        return jdbcTemplate.query(sql, medicamentoMapper);
    }

    public Optional<Medicamento> findById(int id) {
        String sql = """
                SELECT *
                FROM medicamento
                WHERE id_medicamento = ?
                """;
        List<Medicamento> result = jdbcTemplate.query(sql, medicamentoMapper, id);
        return result.stream().findFirst();
    }

    // Útil para verificar stock antes de asignar un tratamiento
    public List<Medicamento> findConStock() {
        String sql = """
                SELECT *
                FROM medicamento
                WHERE stock > 0
                ORDER BY nombre
                """;
        return jdbcTemplate.query(sql, medicamentoMapper);
    }

    // INSERT: respeta chk_med_nombre (>= 3 chars),
    //         chk_med_precio (> 0), chk_med_stock (>= 0),
    //         chk_med_fecha_vencimiento (> 2000-01-01),
    //         fk_medicamento_admin (id_administrador debe existir)
    public void save(Medicamento medicamento) {
        String sql = """
                INSERT INTO medicamento
                (id_administrador, nombre, descripcion,
                 precio, stock, fecha_vencimiento)
                VALUES (?, ?, ?, ?, ?, ?)
                """;
        jdbcTemplate.update(
                sql,
                medicamento.getIdAdministrador(),
                medicamento.getNombre(),
                medicamento.getDescripcion(),
                medicamento.getPrecio(),
                medicamento.getStock(),
                medicamento.getFechaVencimiento()
        );
    }

    public void update(Medicamento medicamento) {
        String sql = """
                UPDATE medicamento
                SET nombre            = ?,
                    descripcion       = ?,
                    precio            = ?,
                    stock             = ?,
                    fecha_vencimiento = ?
                WHERE id_medicamento = ?
                """;
        jdbcTemplate.update(
                sql,
                medicamento.getNombre(),
                medicamento.getDescripcion(),
                medicamento.getPrecio(),
                medicamento.getStock(),
                medicamento.getFechaVencimiento(),
                medicamento.getIdMedicamento()
        );
    }

    // Descuenta unidades del stock; útil al registrar un tratamiento
    public void reducirStock(int idMedicamento, int cantidad) {
        String sql = """
                UPDATE medicamento
                SET stock = stock - ?
                WHERE id_medicamento = ?
                  AND stock >= ?
                """;
        jdbcTemplate.update(sql, cantidad, idMedicamento, cantidad);
    }

    // DELETE restringido: no se puede borrar si está en un tratamiento
    // (fk_tratamiento_medicamento ON DELETE RESTRICT)
    public void deleteById(int id) {
        String sql = """
                DELETE FROM medicamento
                WHERE id_medicamento = ?
                """;
        jdbcTemplate.update(sql, id);
    }
}