package com.ternurines.controller;

import com.ternurines.model.Producto;
import com.ternurines.repository.ProductoRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/productos")
public class ProductoController {

    private final ProductoRepository productoRepository;

    public ProductoController(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    @GetMapping
    public ResponseEntity<List<Producto>> findAll() {
        return ResponseEntity.ok(productoRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Producto> findById(@PathVariable int id) {
        return productoRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/{id}/comprar")
    public ResponseEntity<?> comprar(@PathVariable int id) {
        return productoRepository.findById(id)
                .map(producto -> {
                    if (producto.getStock() <= 0) {
                        return ResponseEntity.badRequest().build();
                    }

                    int rowsAffected = productoRepository.reducirStock(id, 1);
                    if (rowsAffected == 0) {
                        return ResponseEntity.badRequest().build();
                    }

                    return productoRepository.findById(id)
                            .map(ResponseEntity::ok)
                            .orElse(ResponseEntity.notFound().build());
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Producto> save(@RequestBody Producto producto) {
        productoRepository.save(producto);
        return ResponseEntity.status(201).body(producto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Producto> update(
            @PathVariable int id,
            @RequestBody Producto producto) {

        if (productoRepository.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        producto.setIdProducto(id);
        productoRepository.update(producto);
        return ResponseEntity.ok(producto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        if (productoRepository.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        productoRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
