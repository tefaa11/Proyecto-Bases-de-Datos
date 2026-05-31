package com.ternurines.controller;

import com.ternurines.model.Cita;
import com.ternurines.repository.CitaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.DateTimeException;
import java.time.format.DateTimeParseException;

@RestController
@RequestMapping("/citas")
public class CitaController {

    private final CitaRepository citaRepository;

    public CitaController(CitaRepository citaRepository) {
        this.citaRepository = citaRepository;
    }

    @GetMapping
    public ResponseEntity<List<Cita>> findAll() {
        return ResponseEntity.ok(citaRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cita> findById(@PathVariable int id) {
        return citaRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/mascota/{idMascota}")
    public ResponseEntity<List<Cita>> findByMascota(@PathVariable int idMascota) {
        return ResponseEntity.ok(citaRepository.findByMascota(idMascota));
    }

    @GetMapping("/veterinario/{idVeterinario}")
    public ResponseEntity<List<Cita>> findByVeterinario(@PathVariable int idVeterinario) {
        return ResponseEntity.ok(citaRepository.findByVeterinario(idVeterinario));
    }

    @PostMapping
    public ResponseEntity<Cita> save(@RequestBody Map<String, Object> body) {
        try {
            Cita cita = new Cita();
            cita.setIdMascota(((Number) body.get("idMascota")).intValue());
            cita.setIdVeterinario(((Number) body.get("idVeterinario")).intValue());
            cita.setIdRecepcionista(((Number) body.getOrDefault("idRecepcionista", 1)).intValue());
            cita.setFecha(LocalDate.parse((String) body.get("fecha")));
            cita.setHora(parseHora((String) body.get("hora")));
            cita.setMotivo((String) body.getOrDefault("motivo", "Consulta general"));
            cita.setEstado((String) body.getOrDefault("estado", "Pendiente"));
            Cita creada = citaRepository.saveAndReturn(cita);
            return ResponseEntity.status(201).body(creada);
        } catch (ClassCastException | IllegalArgumentException | DateTimeException | NullPointerException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Datos de cita invalidos", ex);
        }
    }

    private LocalTime parseHora(String valor) {
        if (valor == null) {
            throw new DateTimeParseException("Hora requerida", "", 0);
        }

        String normalized = valor.trim().toLowerCase().replaceAll("\\s+", "");
        if (normalized.contains("a.m.") || normalized.contains("am")
                || normalized.contains("p.m.") || normalized.contains("pm")) {
            String[] parts = normalized.substring(0, 5).split(":");
            int hour = Integer.parseInt(parts[0]);
            int minute = Integer.parseInt(parts[1]);

            if ((normalized.contains("p.m.") || normalized.contains("pm")) && hour < 12) {
                hour += 12;
            } else if ((normalized.contains("a.m.") || normalized.contains("am")) && hour == 12) {
                hour = 0;
            }

            return LocalTime.of(hour, minute);
        }

        return LocalTime.parse(valor);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Cita> update(
            @PathVariable int id,
            @RequestBody Cita cita) {

        if (citaRepository.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        cita.setIdCita(id);
        citaRepository.update(cita);
        return ResponseEntity.ok(cita);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        if (citaRepository.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        citaRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
