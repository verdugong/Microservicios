package com.catalogo.catalogo.controller;

import com.catalogo.catalogo.model.Product;
import com.catalogo.catalogo.repository.ProductRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductRepository repository;

    public ProductController(ProductRepository repository) {
        this.repository = repository;
    }

    // Endpoint para el Frontend
    @GetMapping
    public List<Product> getAllProducts() {
        return repository.findAll();
    }

    // Endpoint para obtener detalle
    @GetMapping("/{id}")
    public Product getProductById(@PathVariable Long id) {
        return repository.findById(id).orElseThrow();
    }

    // Endpoint para microservicio de Órdenes
    @PostMapping("/{id}/decrease-stock")
    public Product decreaseStock(
            @PathVariable Long id,
            @RequestParam int quantity
    ) {
        Product product = repository.findById(id).orElseThrow();
        product.setStock(product.getStock() - quantity);
        return repository.save(product);
    }
}
