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
    public List<Recepcionista> findAll() {
        return recepcionistaRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Recepcionista> findById(@PathVariable int id) {
        return recepcionistaRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<String> save(@RequestBody Recepcionista recepcionista) {
        int rows = recepcionistaRepository.save(recepcionista);
        return ResponseEntity.ok("Filas insertadas: " + rows);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> update(@PathVariable int id, @RequestBody Recepcionista recepcionista) {
        recepcionista.setIdRecepcionista(id);
        int rows = recepcionistaRepository.update(recepcionista);
        return ResponseEntity.ok("Filas actualizadas: " + rows);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable int id) {
        int rows = recepcionistaRepository.delete(id);
        return ResponseEntity.ok("Filas eliminadas: " + rows);
    }
}