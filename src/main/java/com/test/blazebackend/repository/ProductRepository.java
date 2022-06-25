package com.test.blazebackend.repository;

import com.test.blazebackend.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProductRepository extends MongoRepository<Product, String  > {

    Page<Product> findByNameContainingIgnoreCase(String name, Pageable pageable);
}


