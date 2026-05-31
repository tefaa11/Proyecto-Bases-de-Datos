package com.ternurines.controller;

import com.ternurines.model.Servicio;
import com.ternurines.repository.ServicioRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/servicios")
public class ServicioController {

    private final ServicioRepository servicioRepository;

    public ServicioController(ServicioRepository servicioRepository) {
        this.servicioRepository = servicioRepository;
    }

    @GetMapping
    public ResponseEntity<List<Servicio>> findAll() {
        return ResponseEntity.ok(servicioRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Servicio> findById(@PathVariable int id) {
        return servicioRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Servicio> save(@RequestBody Servicio servicio) {
        servicioRepository.save(servicio);
        return ResponseEntity.status(201).body(servicio);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Servicio> update(
            @PathVariable int id,
            @RequestBody Servicio servicio) {

        if (servicioRepository.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        servicio.setIdServicio(id);
        servicioRepository.update(servicio);
        return ResponseEntity.ok(servicio);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        if (servicioRepository.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        servicioRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
