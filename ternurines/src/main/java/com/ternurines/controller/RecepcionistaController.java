package com.ternurines.controller;

import com.ternurines.model.Recepcionista;
import com.ternurines.repository.RecepcionistaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/recepcionistas")
public class RecepcionistaController {

    private final RecepcionistaRepository recepcionistaRepository;

    public RecepcionistaController(RecepcionistaRepository recepcionistaRepository) {
        this.recepcionistaRepository = recepcionistaRepository;
    }

    @GetMapping
    public ResponseEntity<List<Recepcionista>> findAll() {
        return ResponseEntity.ok(recepcionistaRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Recepcionista> findById(@PathVariable int id) {

        return recepcionistaRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Recepcionista> save(
            @RequestBody Recepcionista recepcionista) {

        recepcionistaRepository.save(recepcionista);

        return ResponseEntity.status(201).body(recepcionista);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Recepcionista> update(
            @PathVariable int id,
            @RequestBody Recepcionista recepcionista) {

        recepcionista.setIdRecepcionista(id);

        int rows = recepcionistaRepository.update(recepcionista);

        if (rows == 0) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(recepcionista);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {

        int rows = recepcionistaRepository.delete(id);

        if (rows == 0) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.noContent().build();
    }
}