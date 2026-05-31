package com.ternurines.controller;

import com.ternurines.model.Tratamiento;
import com.ternurines.repository.TratamientoRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tratamientos")
public class TratamientoController {

    private final TratamientoRepository tratamientoRepository;

    public TratamientoController(TratamientoRepository tratamientoRepository) {
        this.tratamientoRepository = tratamientoRepository;
    }

    @GetMapping
    public ResponseEntity<List<Tratamiento>> findAll() {
        return ResponseEntity.ok(tratamientoRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Tratamiento> findById(@PathVariable int id) {
        return tratamientoRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Tratamiento> save(@RequestBody Tratamiento tratamiento) {
        tratamientoRepository.save(tratamiento);
        return ResponseEntity.status(201).body(tratamiento);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Tratamiento> update(
            @PathVariable int id,
            @RequestBody Tratamiento tratamiento) {

        if (tratamientoRepository.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        tratamiento.setIdTratamiento(id);
        tratamientoRepository.update(tratamiento);
        return ResponseEntity.ok(tratamiento);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        if (tratamientoRepository.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        tratamientoRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
