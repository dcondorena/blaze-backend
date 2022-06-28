package com.test.blazebackend;

import com.test.blazebackend.model.request.OrderRequestDto;
import com.test.blazebackend.model.request.ProductOrderRequestDto;
import com.test.blazebackend.model.request.ProductRequestDto;
import com.test.blazebackend.model.request.UpdateOrderRequestDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BlazeBackendApplicationTests {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void getProductPage() throws Exception {
        assertThat(this.restTemplate.getForEntity("http://localhost:" + port + "/api/v1/products",
                Object.class).getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void saveProduct() throws Exception {
        ProductRequestDto productRequestDto = new ProductRequestDto(
                "PPP", "Cakes", BigDecimal.valueOf(25), true
        );
        assertThat(this.restTemplate.postForEntity("http://localhost:" + port + "/api/v1/products",
                productRequestDto, Object.class).getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void updateProduct() throws Exception {
        ProductRequestDto productRequestDto = new ProductRequestDto(
                "Chocolate Cakesssssssss", "Cakes", BigDecimal.valueOf(12), true
        );
        this.restTemplate.put("http://localhost:" + port + "/api/v1/products/62b88e5510bcac62b677a355", productRequestDto);
    }


    @Test
    public void getOrderPage() throws Exception {
        assertThat(this.restTemplate.getForEntity("http://localhost:" + port + "/api/v1/orders",
                Object.class).getStatusCode()).isEqualTo(HttpStatus.OK);
    }

//    @Test
//    public void saveOrder() throws Exception {
//        List<ProductOrderRequestDto> productOrderRequestDtoList = new ArrayList<>();
//        productOrderRequestDtoList.add(new ProductOrderRequestDto(
//                "62b692c6a311f10e7f3f35da",
//                "Chocolate Cake",
//                "Cakessss",
//                BigDecimal.valueOf(60),
//                true
//        ));
//        OrderRequestDto productRequestDto = new OrderRequestDto(
//                "Namii Castro", productOrderRequestDtoList
//        );
//        assertThat(this.restTemplate.postForEntity("http://localhost:" + port + "/api/v1/orders",
//                productRequestDto, Object.class).getStatusCode()).isEqualTo(HttpStatus.OK);
//    }

//    @Test
//    public void updateOrder() throws Exception {
//        List<ProductOrderRequestDto> productOrderRequestDtoList = new ArrayList<>();
//        productOrderRequestDtoList.add(new ProductOrderRequestDto(
//                "62b692c6a311f10e7f3f35da",
//                "Chocolate Cake",
//                "Cakessss",
//                BigDecimal.valueOf(60),
//                true
//        ));
//        UpdateOrderRequestDto updateOrderRequestDto = new UpdateOrderRequestDto(
//                "Rejected", productOrderRequestDtoList
//        );
//       this.restTemplate.put("http://localhost:" + port + "/api/v1/orders/62ba42d66b0f1e2ae983677a",
//               updateOrderRequestDto);
//    }


}
