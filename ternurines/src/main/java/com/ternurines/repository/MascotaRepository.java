package com.ternurines.repository;

import com.ternurines.model.Cliente;
import com.ternurines.model.Mascota;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class MascotaRepository {
    private final JdbcTemplate jdbcTemplate;
    public MascotaRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Mascota> mascotaMapper =
        (rs, rowNum) -> {

            Cliente cliente = new Cliente();
            cliente.setIdCliente(
                rs.getInt("id_cliente"));
            cliente.setNombre(
                rs.getString("nombre_cliente"));
            Mascota mascota = new Mascota();
            mascota.setIdMascota(
                rs.getInt("id_mascota"));
            mascota.setCliente(cliente);
            mascota.setNombre(
                rs.getString("nombre"));
            mascota.setEspecie(
                rs.getString("especie"));
            mascota.setRaza(
                rs.getString("raza"));
            mascota.setEdad(
                rs.getInt("edad"));
            mascota.setPeso(
                rs.getDouble("peso"));
            mascota.setSexo(
                rs.getString("sexo"));
        return mascota;
    };

    public List<Mascota> findAll() {
        String sql = """
                SELECT m.*,
                       c.nombre AS nombre_cliente
                FROM mascota m
                JOIN cliente c
                ON m.id_cliente = c.id_cliente
                """;
        return jdbcTemplate.query(
                sql,
                mascotaMapper
        );
    }

    public Optional<Mascota> findById(int id) {
        String sql = """
                SELECT m.*,
                       c.nombre AS nombre_cliente
                FROM mascota m
                JOIN cliente c
                ON m.id_cliente = c.id_cliente
                WHERE m.id_mascota = ?
                """;
        List<Mascota> mascotas =
                jdbcTemplate.query(
                        sql,
                        mascotaMapper,
                        id
                );
        return mascotas
                .stream()
                .findFirst();
    }

    public void save(Mascota mascota) {
        String sql = """
                INSERT INTO mascota
                (id_cliente, nombre,
                 especie, raza,
                 edad, peso, sexo)
                VALUES (?, ?, ?, ?, ?, ?, ?)
                """;

        jdbcTemplate.update(
                sql,
                mascota.getCliente().getIdCliente(),
                mascota.getNombre(),
                mascota.getEspecie(),
                mascota.getRaza(),
                mascota.getEdad(),
                mascota.getPeso(),
                mascota.getSexo()
        );
    }

    public void update(Mascota mascota) {
        String sql = """
                UPDATE mascota
                SET id_cliente = ?,
                    nombre = ?,
                    especie = ?,
                    raza = ?,
                    edad = ?,
                    peso = ?,
                    sexo = ?
                WHERE id_mascota = ?
                """;
        jdbcTemplate.update(
                sql,
                mascota.getCliente().getIdCliente(),
                mascota.getNombre(),
                mascota.getEspecie(),
                mascota.getRaza(),
                mascota.getEdad(),
                mascota.getPeso(),
                mascota.getSexo(),
                mascota.getIdMascota()
        );
    }

    public void deleteById(int id) {
        String sql = """
                DELETE FROM mascota
                WHERE id_mascota = ?
                """;
        jdbcTemplate.update(sql, id);
    }
}
