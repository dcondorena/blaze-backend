package com.test.blazebackend.api;

import com.test.blazebackend.bl.ProductBl;
import com.test.blazebackend.dao.entity.Product;
import com.test.blazebackend.model.request.ProductRequestDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
    ProductBl productBl;

    @GetMapping("/products")
    public ResponseEntity<Map<String, Object>> getAllProductsPage(
            @RequestParam(required = false) String name,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "3") int size) {
        LOGGER.info("REQUEST: Initiating Product Pagination Request Page:{} , Size: {}, Name: {}", page, size, name);
        Map<String, Object> data = productBl.getProductPage(page, size, name);
        LOGGER.info("SUCCESS-REQUEST: Product Pagination Request Successfull");
        return new ResponseEntity<>(data, HttpStatus.OK);
    }

    @PostMapping("/products")
    public ResponseEntity<Product> createProduct(@RequestBody ProductRequestDto productRequestDto) {
        LOGGER.info("REQUEST: Initiating Post Product Request :{}", productRequestDto);
        Product data = productBl.createProduct(productRequestDto);
        LOGGER.info("SUCCESS-REQUEST: Post Product Request Successfull");
        return new ResponseEntity<>(data, HttpStatus.OK);
    }

    @PutMapping("/products/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable("id") String id, @RequestBody ProductRequestDto productRequestDto) {
        LOGGER.info("REQUEST: Initiating Update Product Request By ProductId :{}", id);
        Product data = productBl.updateProduct(id, productRequestDto);
        LOGGER.info("SUCCESS-REQUEST: Update Product Request Successfull");
        return new ResponseEntity<>(data, HttpStatus.OK);
    }


    @DeleteMapping("/products/{id}")
    public ResponseEntity<HttpStatus> deleteProduct(@PathVariable("id") String id) {
        LOGGER.info("REQUEST: Initiating Delete Product Request By ProductId :{}", id);
        productBl.deleteProduct(id);
        LOGGER.info("SUCCESS-REQUEST: Delete Product Request Successfull");
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
