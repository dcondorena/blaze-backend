package com.test.blazebackend.api;

import com.test.blazebackend.bl.OrderBl;
import com.test.blazebackend.dao.entity.Order;
import com.test.blazebackend.model.request.OrderRequestDto;
import com.test.blazebackend.model.request.UpdateOrderRequestDto;
import com.test.blazebackend.model.response.OrderResponseDto;
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
public class OrderController {
    private static final Logger LOGGER = LoggerFactory.getLogger(OrderController.class);

    @Autowired
    OrderBl orderBl;

    @GetMapping("/orders")
    public ResponseEntity<Map<String, Object>> getAllProductsPage(
            @RequestParam(required = false) Integer orderNumber,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "3") int size) {
        LOGGER.info("REQUEST: Initiating Product Pagination Request Page:{} , Size: {}, OrderNumber: {}", page, size, orderNumber);
        Map<String, Object> data = orderBl.getOrderPage(orderNumber,page, size);
        LOGGER.info("SUCCESS-REQUEST: Product Pagination Request Successfull");
        return new ResponseEntity<>(data, HttpStatus.OK);
    }

    @GetMapping("/orders/{id}")
    public ResponseEntity<OrderResponseDto> getOrderDetail(
            @PathVariable("id") String id) {
        LOGGER.info("REQUEST: Initiating Request to Get Order Detail by OrderId: {}", id);
        OrderResponseDto data = orderBl.getOrderDetail(id);
        LOGGER.info("SUCCESS-REQUEST: Request to Get Order Detail by OrderId: {} Successfull", id);
        return new ResponseEntity<>(data, HttpStatus.OK);
    }


    @PostMapping("/orders")
    public ResponseEntity<Order> createOrder(@RequestBody OrderRequestDto orderRequestDto) {
        LOGGER.info("REQUEST: Initiating Post Order Request :{}", orderRequestDto);
        Order data = orderBl.createOrder(orderRequestDto);
        LOGGER.info("SUCCESS-REQUEST: Post Order Request Successfull");
        return new ResponseEntity<>(data, HttpStatus.OK);
    }

    @PutMapping("/orders/{id}")
    public ResponseEntity<Order> updateOrder(@PathVariable("id") String id, @RequestBody UpdateOrderRequestDto updateOrderRequestDto) {
        LOGGER.info("REQUEST: Initiating Update Order Request By OrderId :{}", id);
        Order data = orderBl.updateOrder(id, updateOrderRequestDto);
        LOGGER.info("SUCCESS-REQUEST: Update Order Request Successfull");
        return new ResponseEntity<>(data, HttpStatus.OK);
    }


    @DeleteMapping("/orders/{id}")
    public ResponseEntity<HttpStatus> deleteOrder(@PathVariable("id") String id) {
        LOGGER.info("REQUEST: Initiating Delete Order Request By ProductId :{}", id);
        orderBl.deleteOrder(id);
        LOGGER.info("SUCCESS-REQUEST: Delete Order Request Successfull");
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
