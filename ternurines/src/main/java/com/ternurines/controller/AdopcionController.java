package com.ternurines.controller;

import com.ternurines.model.Adopcion;
import com.ternurines.model.Adoptante;
import com.ternurines.repository.AdopcionRepository;
import com.ternurines.repository.AdoptanteRepository;
import com.ternurines.repository.ClienteRepository;
import com.ternurines.repository.MascotaAdopcionRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/adopciones")
public class AdopcionController {

    private final AdopcionRepository adopcionRepository;
    private final AdoptanteRepository adoptanteRepository;
    private final ClienteRepository clienteRepository;
    private final MascotaAdopcionRepository mascotaAdopcionRepository;

    public AdopcionController(
            AdopcionRepository adopcionRepository,
            AdoptanteRepository adoptanteRepository,
            ClienteRepository clienteRepository,
            MascotaAdopcionRepository mascotaAdopcionRepository) {
        this.adopcionRepository = adopcionRepository;
        this.adoptanteRepository = adoptanteRepository;
        this.clienteRepository = clienteRepository;
        this.mascotaAdopcionRepository = mascotaAdopcionRepository;
    }

    // GET LISTA
    @GetMapping
    public ResponseEntity<List<Adopcion>> findAll() {
        return ResponseEntity.ok(adopcionRepository.findAll());
    }

    // GET POR ID
    @GetMapping("/{idAdoptante}/{idMascotaAdopcion}")
    public ResponseEntity<Adopcion> findById(
            @PathVariable int idAdoptante,
            @PathVariable int idMascotaAdopcion) {

        return adopcionRepository.findById(idAdoptante, idMascotaAdopcion)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // POST -> 201 CREATED
    @PostMapping
    public ResponseEntity<?> save(@RequestBody Map<String, Object> body) {
        int idMascotaAdopcion = ((Number) body.get("idMascotaAdopcion")).intValue();
        int idCliente = body.containsKey("idCliente")
                ? ((Number) body.get("idCliente")).intValue()
                : ((Number) body.get("idAdoptante")).intValue();

        var cliente = clienteRepository.findById(idCliente);
        if (cliente.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Cliente no encontrado"));
        }

        var mascota = mascotaAdopcionRepository.findById(idMascotaAdopcion);
        if (mascota.isEmpty() || !"Disponible".equalsIgnoreCase(mascota.get().getEstadoAdopcion())) {
            return ResponseEntity.badRequest().body(Map.of("error", "Mascota no disponible para adopción"));
        }

        var c = cliente.get();
        Adoptante adoptante = adoptanteRepository.findByCorreo(c.getCorreo())
                .orElseGet(() -> {
                    Adoptante nuevo = new Adoptante();
                    nuevo.setNombre(c.getNombre());
                    nuevo.setDocumento(c.getDocumento());
                    nuevo.setTelefono(c.getTelefono());
                    nuevo.setDireccion(c.getDireccion());
                    nuevo.setCorreo(c.getCorreo());
                    return adoptanteRepository.saveAndReturn(nuevo);
                });

        Adopcion adopcion = new Adopcion();
        adopcion.setIdAdoptante(adoptante.getIdAdoptante());
        adopcion.setIdMascotaAdopcion(idMascotaAdopcion);
        adopcion.setFechaAdopcion(java.time.LocalDate.now());
        adopcionRepository.save(adopcion);
        mascotaAdopcionRepository.actualizarEstado(idMascotaAdopcion, "En proceso");

        return ResponseEntity.status(201).body(adopcion);
    }

    // PUT -> 200 OK o 404
    @PutMapping("/{idAdoptante}/{idMascotaAdopcion}")
    public ResponseEntity<Adopcion> update(
            @PathVariable int idAdoptante,
            @PathVariable int idMascotaAdopcion,
            @RequestBody Adopcion adopcion) {

        adopcion.setIdAdoptante(idAdoptante);
        adopcion.setIdMascotaAdopcion(idMascotaAdopcion);

        int rows = adopcionRepository.update(adopcion);

        if (rows == 0) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(adopcion);
    }

    // DELETE -> 204 NO CONTENT
    @DeleteMapping("/{idAdoptante}/{idMascotaAdopcion}")
    public ResponseEntity<Void> delete(
            @PathVariable int idAdoptante,
            @PathVariable int idMascotaAdopcion) {

        int rows = adopcionRepository.delete(idAdoptante, idMascotaAdopcion);

        if (rows == 0) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.noContent().build();
    }
}
