package com.ternurines.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.ternurines.model.Producto;

@Repository
public class ProductoRepository {

    private final JdbcTemplate jdbcTemplate;

    public ProductoRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Producto> productoMapper =
        (rs, rowNum) -> {
            Producto producto = new Producto();
            producto.setIdProducto(rs.getInt("id_producto"));
            producto.setIdAdministrador(rs.getInt("id_administrador"));
            producto.setNombre(rs.getString("nombre"));
            producto.setDescripcion(rs.getString("descripcion"));
            producto.setPrecio(rs.getDouble("precio"));
            producto.setStock(rs.getInt("stock"));
            // fecha_vencimiento es nullable en producto (a diferencia de medicamento)
            producto.setFechaVencimiento(rs.getDate("fecha_vencimiento"));
            return producto;
        };

    // Solo rol_admin_general tiene ALL PRIVILEGES sobre esta tabla
    public List<Producto> findAll() {
        String sql = "SELECT * FROM producto";
        return jdbcTemplate.query(sql, productoMapper);
    }

    public Optional<Producto> findById(int id) {
        String sql = """
                SELECT *
                FROM producto
                WHERE id_producto = ?
                """;
        List<Producto> result = jdbcTemplate.query(sql, productoMapper, id);
        return result.stream().findFirst();
    }

    // Útil para mostrar solo productos disponibles en tienda/recepción
    public List<Producto> findConStock() {
        String sql = """
                SELECT *
                FROM producto
                WHERE stock > 0
                ORDER BY nombre
                """;
        return jdbcTemplate.query(sql, productoMapper);
    }

    // INSERT: respeta chk_prod_nombre (>= 3 chars),
    //         chk_prod_precio (> 0), chk_prod_stock (>= 0),
    //         chk_prod_fecha_vencimiento (NULL o > 2000-01-01),
    //         fk_producto_admin (id_administrador debe existir)
    public void save(Producto producto) {
        String sql = """
                INSERT INTO producto
                (id_administrador, nombre, descripcion,
                 precio, stock, fecha_vencimiento)
                VALUES (?, ?, ?, ?, ?, ?)
                """;
        jdbcTemplate.update(
                sql,
                producto.getIdAdministrador(),
                producto.getNombre(),
                producto.getDescripcion(),
                producto.getPrecio(),
                producto.getStock(),
                producto.getFechaVencimiento()   // puede ser null
        );
    }

    public void update(Producto producto) {
        String sql = """
                UPDATE producto
                SET nombre            = ?,
                    descripcion       = ?,
                    precio            = ?,
                    stock             = ?,
                    fecha_vencimiento = ?
                WHERE id_producto = ?
                """;
        jdbcTemplate.update(
                sql,
                producto.getNombre(),
                producto.getDescripcion(),
                producto.getPrecio(),
                producto.getStock(),
                producto.getFechaVencimiento(),   // puede ser null
                producto.getIdProducto()
        );
    }

    // Descuenta unidades del stock al realizar una venta
    public void reducirStock(int idProducto, int cantidad) {
        String sql = """
                UPDATE producto
                SET stock = stock - ?
                WHERE id_producto = ?
                  AND stock >= ?
                """;
        jdbcTemplate.update(sql, cantidad, idProducto, cantidad);
    }

    public void deleteById(int id) {
        String sql = """
                DELETE FROM producto
                WHERE id_producto = ?
                """;
        jdbcTemplate.update(sql, id);
    }
}