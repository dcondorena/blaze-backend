package com.test.blazebackend.api;

import com.test.blazebackend.model.Product;
import com.test.blazebackend.repository.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v1")
public class ProductController {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProductController.class);

    @Autowired
    ProductRepository productRepository;

    @GetMapping("/products")
    public ResponseEntity<Map<String, Object>> getAllProductsPage(
            @RequestParam(required = false) String name,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "3") int size) {
        LOGGER.info("REQUEST: Init Request");
        try {
            List<Product> products = new ArrayList<Product>();
            Pageable paging = PageRequest.of(page, size);

            Page<Product> productPage;
            if (name == null) {
                productPage = productRepository.findAll(paging);
                LOGGER.info("ProductPage: " + productPage);
            } else {
                productPage = productRepository.findByNameContainingIgnoreCase(name, paging);
            }
            products = productPage.getContent();

            Map<String, Object> response = new HashMap<>();
            response.put("products", products);
            response.put("currentPage", productPage.getNumber());
            response.put("totalItems", productPage.getTotalElements());
            response.put("totalPages", productPage.getTotalPages());

            System.out.println("Response: " + response);

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            System.out.println("Exception" + e);
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/products")
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {

        LOGGER.info("REQUEST: Init Request Post Product");
        try {
            Product _product = productRepository.save(new Product(product.getName(), product.getCategory(), product.getUnitPrice(), product.getActive()));
            return new ResponseEntity<>(_product, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/products/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable("id") String id, @RequestBody Product product) {
        Optional<Product> tutorialData = productRepository.findById(id);

        if (tutorialData.isPresent()) {
            Product _product = tutorialData.get();
            _product.setName(product.getName());
            _product.setCategory(product.getCategory());
            _product.setCategory(product.getCategory());
            _product.setActive(product.getActive());
            return new ResponseEntity<>(productRepository.save(_product), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    @DeleteMapping("/products/{id}")
    public ResponseEntity<HttpStatus> deleteProduct(@PathVariable("id") String id) {
        try {
            productRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
