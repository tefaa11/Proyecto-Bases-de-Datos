package com.ternurines.controller;

import com.ternurines.model.VistaAgendaDiaria;
import com.ternurines.repository.VistaAgendaDiariaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/vistas")
public class VistaAgendaDiariaController {

    private final VistaAgendaDiariaRepository vistaAgendaDiariaRepository;

    public VistaAgendaDiariaController(VistaAgendaDiariaRepository vistaAgendaDiariaRepository) {
        this.vistaAgendaDiariaRepository = vistaAgendaDiariaRepository;
    }

    @GetMapping("/agenda-diaria")
    public ResponseEntity<List<VistaAgendaDiaria>> agendaDiaria() {
        return ResponseEntity.ok(vistaAgendaDiariaRepository.findAll());
    }
}