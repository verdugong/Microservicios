package com.catalogo.catalogo.repository;

import com.catalogo.catalogo.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
