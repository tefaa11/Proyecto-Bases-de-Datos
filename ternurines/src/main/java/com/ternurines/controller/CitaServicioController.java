package com.ternurines.controller;

import com.ternurines.model.CitaServicio;
import com.ternurines.repository.CitaServicioRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cita-servicios")
public class CitaServicioController {

    private final CitaServicioRepository citaServicioRepository;

    public CitaServicioController(CitaServicioRepository citaServicioRepository) {
        this.citaServicioRepository = citaServicioRepository;
    }

    @GetMapping
    public ResponseEntity<List<CitaServicio>> findAll() {
        return ResponseEntity.ok(citaServicioRepository.findAll());
    }

    @GetMapping("/cita/{idCita}")
    public ResponseEntity<List<CitaServicio>> findByIdCita(@PathVariable int idCita) {
        return ResponseEntity.ok(citaServicioRepository.findByIdCita(idCita));
    }

    @GetMapping("/servicio/{idServicio}")
    public ResponseEntity<List<CitaServicio>> findByIdServicio(@PathVariable int idServicio) {
        return ResponseEntity.ok(citaServicioRepository.findByIdServicio(idServicio));
    }

    @PostMapping
    public ResponseEntity<CitaServicio> save(@RequestBody CitaServicio citaServicio) {
        citaServicioRepository.save(citaServicio);
        return ResponseEntity.status(201).body(citaServicio);
    }

    @DeleteMapping("/{idCita}/{idServicio}")
    public ResponseEntity<Void> delete(
            @PathVariable int idCita,
            @PathVariable int idServicio) {

        boolean exists = citaServicioRepository.findByIdCita(idCita).stream()
                .anyMatch(item -> item.getIdServicio() == idServicio);

        if (!exists) {
            return ResponseEntity.notFound().build();
        }

        citaServicioRepository.deleteByIdCitaAndIdServicio(idCita, idServicio);
        return ResponseEntity.noContent().build();
    }
}
