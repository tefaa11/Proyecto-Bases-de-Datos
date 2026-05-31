package com.ternurines.controller;

import com.ternurines.model.Mascota;
import com.ternurines.repository.MascotaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/mascotas")
public class MascotaController {

    private final MascotaRepository mascotaRepository;

    public MascotaController(MascotaRepository mascotaRepository) {
        this.mascotaRepository = mascotaRepository;
    }

    // GET LISTA
    @GetMapping
    public ResponseEntity<List<Mascota>> findAll() {
        return ResponseEntity.ok(mascotaRepository.findAll());
    }

    // GET POR ID
    @GetMapping("/{id}")
    public ResponseEntity<Mascota> findById(@PathVariable int id) {

        return mascotaRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/cliente/{idCliente}")
    public ResponseEntity<List<Mascota>> findByCliente(@PathVariable int idCliente) {
        return ResponseEntity.ok(mascotaRepository.findByCliente(idCliente));
    }

    // POST
    @PostMapping
    public ResponseEntity<Mascota> save(@RequestBody Mascota mascota) {

        mascotaRepository.save(mascota);

        return ResponseEntity.status(201).body(mascota);
    }

    // PUT
    @PutMapping("/{id}")
    public ResponseEntity<Mascota> update(
            @PathVariable int id,
            @RequestBody Mascota mascota) {

        mascota.setIdMascota(id);

        if (mascotaRepository.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        mascotaRepository.update(mascota);

        return ResponseEntity.ok(mascota);
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {

        if (mascotaRepository.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        mascotaRepository.deleteById(id);

        return ResponseEntity.noContent().build();
    }
}