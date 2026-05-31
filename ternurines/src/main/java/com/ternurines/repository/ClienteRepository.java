package com.ternurines.repository;

import com.ternurines.model.Cliente;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class ClienteRepository {
    private final JdbcTemplate jdbcTemplate;
    public ClienteRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Cliente> clienteMapper =
        (rs, rowNum) -> {
            Cliente cliente = new Cliente();
            cliente.setIdCliente(
                rs.getInt("id_cliente"));
            cliente.setNombre(
                rs.getString("nombre"));
            cliente.setDocumento(
                rs.getString("documento"));
            cliente.setTelefono(
                rs.getString("telefono"));
            cliente.setDireccion(
                rs.getString("direccion"));
            cliente.setCorreo(
                rs.getString("correo"));
            cliente.setContrasenia(
                rs.getString("contrasena"));
        return cliente;
    };

    public List<Cliente> findAll() {

        String sql =
                "SELECT * FROM cliente";

        return jdbcTemplate.query(
                sql,
                clienteMapper
        );
    }

    public Optional<Cliente> findById(int id) {
        String sql = """
                SELECT *
                FROM cliente
                WHERE id_cliente = ?
                """;
        List<Cliente> clientes =
                jdbcTemplate.query(
                        sql,
                        clienteMapper,
                        id
                );
        return clientes
                .stream()
                .findFirst();
    }

    public Optional<Cliente> findByCorreo(String correo) {
        String sql = """
                SELECT *
                FROM cliente
                WHERE correo = ?
                """;
        List<Cliente> clientes =
                jdbcTemplate.query(
                        sql,
                        clienteMapper,
                        correo
                );
        return clientes.stream().findFirst();
    }

    public void save(Cliente cliente) {
        String sql = """
                INSERT INTO cliente
                (nombre, documento,
                 telefono, direccion,
                 correo, contrasena)
                VALUES (?, ?, ?, ?, ?, ?)
                """;
        jdbcTemplate.update(
                sql,
                cliente.getNombre(),
                cliente.getDocumento(),
                cliente.getTelefono(),
                cliente.getDireccion(),
                cliente.getCorreo(),
                cliente.getContrasenia()
        );
    }

    // ACTUALIZAR
    public void update(Cliente cliente) {
        String sql = """
                UPDATE cliente
                SET nombre = ?,
                    documento = ?,
                    telefono = ?,
                    direccion = ?,
                    correo = ?,
                    contrasena = ?
                WHERE id_cliente = ?
                """;
        jdbcTemplate.update(
                sql,
                cliente.getNombre(),
                cliente.getDocumento(),
                cliente.getTelefono(),
                cliente.getDireccion(),
                cliente.getCorreo(),
                cliente.getContrasenia(),
                cliente.getIdCliente()
        );
    }

    public void deleteById(int id) {
        String sql = """
                DELETE FROM cliente
                WHERE id_cliente = ?
                """;
        jdbcTemplate.update(sql, id);
    }
}