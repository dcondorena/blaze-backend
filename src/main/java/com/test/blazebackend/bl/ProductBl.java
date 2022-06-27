package com.test.blazebackend.bl;

import com.test.blazebackend.dao.entity.Product;
import com.test.blazebackend.dao.repository.ProductRepository;
import com.test.blazebackend.exception.ServiceException;
import com.test.blazebackend.model.request.ProductRequestDto;
import com.test.blazebackend.model.response.ProductResponseDto;
import com.test.blazebackend.util.MapperUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class ProductBl {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductBl.class);
    private final ProductRepository productRepository;
    private final MapperUtil mapperUtil;

    @Autowired
    public ProductBl(ProductRepository productRepository, MapperUtil mapperUtil) {
        this.productRepository = productRepository;
        this.mapperUtil = mapperUtil;
    }

    public Map<String, Object> getProductPage(int page, int size, String name) {
        LOGGER.info("PRODUCT-BL: Starting paginated product request");
        try {
            Pageable paging = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "unitPrice"));
            Page<Product> productPage;
            if (name == null) {
                LOGGER.info("QUERY-DB: Consult the database to obtain a list of products");
                productPage = productRepository.findAll(paging);
                LOGGER.info("SUCCESS-QUERY-DB: ProductPage: " + productPage);
            } else {
                LOGGER.info("QUERY-DB: Consult the database to obtain a list of products by name");
                productPage = productRepository.findByNameContainingIgnoreCase(name, paging);
                LOGGER.info("SUCCESS-QUERY-DB: ProductPage By Name: " + productPage);
            }

            Stream<ProductResponseDto> productResponseDtoStream = mapperUtil.mapProductResponseDto(productPage.getContent().stream());

            List<ProductResponseDto> products = productResponseDtoStream.collect(Collectors.toList());

            Map<String, Object> response = new HashMap<>();
            response.put("products", products);
            response.put("currentPage", productPage.getNumber());
            response.put("totalItems", productPage.getTotalElements());
            response.put("totalPages", productPage.getTotalPages());


            LOGGER.debug("PRODUCT-BL-SUCCES: Response:{} ", response);
            LOGGER.info("PRODUCT-BL-SUCCESS: Paginated product request successfull");
            return response;
        } catch (Exception error) {
            LOGGER.error("PRODUCT-BL-ERROR: Error:{} ", error.getMessage());
            throw new ServiceException(500, "Paginated Product Request Failed");
        }

    }

    public Product createProduct(ProductRequestDto productRequestDto) {
        LOGGER.info("PRODUCT-BL: Starting product creation  request");
        try {
            LOGGER.info("QUERY-DB: Starting save product");
            Product _product = productRepository.save(new Product(productRequestDto.getName(), productRequestDto.getCategory(), productRequestDto.getUnitPrice(), productRequestDto.getActive()));
            LOGGER.info("QUERY-DB-SUCCESS: Save product successfull");
            return _product;
        } catch (Exception error) {
            LOGGER.error("PRODUCT-BL-ERROR: Error:{} ", error.getMessage());
            throw new ServiceException(500, "Product Creation Failed");
        }
    }

    public Product updateProduct(String productId, ProductRequestDto productRequestDto) {
        LOGGER.info("PRODUCT-BL: Starting update product request");

        LOGGER.info("QUERY-DB: Starting find product");
        Optional<Product> productData = productRepository.findById(productId);
        LOGGER.info("QUERY-DB-SUCCESS: Find product successfull");
        if (productData.isPresent()) {
            Product _product = productData.get();
            _product.setName(productRequestDto.getName());
            _product.setCategory(productRequestDto.getCategory());
            _product.setUnitPrice(productRequestDto.getUnitPrice());
            _product.setActive(productRequestDto.getActive());
            LOGGER.info("QUERY-DB: Initiating query to update product");
            Product response = productRepository.save(_product);
            LOGGER.info("QUERY-DB-SUCCESS: Query to update product successfull");
            return response;
        } else {
            throw new ServiceException(404, "Product Not Found");
        }
    }

    public void deleteProduct(String productId) {
        LOGGER.info("PRODUCT-BL: Starting update product request");
        try {
            LOGGER.info("QUERY-DB: Initiating query to delete product");
            productRepository.deleteById(productId);
            LOGGER.info("QUERY-DB-SUCCESS: Query to delete product successfull");
        } catch (Exception e) {
            LOGGER.error("PRODUCT-BL-ERROR: Error:{} ", e.getMessage());
            throw new ServiceException(500, "Deleted Product Failed");
        }
    }

}
