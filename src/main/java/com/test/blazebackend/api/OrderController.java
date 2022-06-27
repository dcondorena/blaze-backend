package com.test.blazebackend.api;

import com.test.blazebackend.dao.entity.Order;
import com.test.blazebackend.dao.entity.Product;
import com.test.blazebackend.dao.repository.OrderRepository;
import com.test.blazebackend.model.request.OrderRequestDto;
import com.test.blazebackend.model.request.ProductRequestDto;
import com.test.blazebackend.model.request.UpdateOrderRequestDto;
import com.test.blazebackend.model.response.OrderResponseDto;
import com.test.blazebackend.model.response.ProductResponseDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.transform.FieldVisitorTee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v1")
public class OrderController {
    private static final Logger LOGGER = LoggerFactory.getLogger(OrderController.class);

    @Autowired
    OrderRepository orderRepository;

    @GetMapping("/orders")
    public ResponseEntity<Map<String, Object>> getAllOrderPage(
            @RequestParam(required = false) String orderNumber,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "3") int size) {
        LOGGER.info("REQUEST: Init Request");
        try {
            Pageable paging = PageRequest.of(page, size);

            Page<Order> orderPage;
            if (orderNumber == null) {
                orderPage = orderRepository.findAll(paging);
            } else {
                orderPage = orderRepository.findByOrderNumberContainingIgnoreCase(orderNumber, paging);
            }

            Stream<OrderResponseDto> orderResponseDtoStream = orderPage.getContent().stream().map(order -> {
                /*
                 * Convert Date
                 */
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                df.setTimeZone(TimeZone.getTimeZone("America/La_Paz"));
                df.format(order.getRegisterDate());
                /*
                 * Map Products
                 * */
                Stream<ProductResponseDto> productResponseDtoStream = order.getItems().stream().map(item -> {
                    return new ProductResponseDto(
                            item.getProductId(), item.getName(), item.getCategory(), item.getUnitPrice(), item.getActive()
                    );
                });
                List<ProductResponseDto> productResponseDtoList = productResponseDtoStream.collect(Collectors.toList());
                /*
                 * Map Orders
                 * */
                return new OrderResponseDto(
                        order.getOrderId(),
                        order.getOrderNumber(),
                        order.getStatus(),
                        df.format(order.getRegisterDate()),
                        order.getCustomer(),
                        order.getSubtotal(),
                        order.getCityTaxAmount(),
                        order.getCountyTaxAmount(),
                        order.getStateTaxAmount(),
                        order.getFederalTaxAmount(),
                        order.getTotalTaxesAmount(),
                        order.getTotalAmount(),
                        productResponseDtoList
                );
            });


            List<OrderResponseDto> orders = orderResponseDtoStream.collect(Collectors.toList());


            Map<String, Object> response = new HashMap<>();
            response.put("orders", orders);
            response.put("currentPage", orderPage.getNumber());
            response.put("totalItems", orderPage.getTotalElements());
            response.put("totalPages", orderPage.getTotalPages());

            System.out.println("Response: " + response);

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            System.out.println("Exception" + e);
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/orders/{id}")
    public ResponseEntity<OrderResponseDto> getOrderDetail(
            @PathVariable("id") String id) {
        LOGGER.info("REQUEST: Init Request");
        Optional<Order> orderData = orderRepository.findById(id);

        if (orderData.isPresent()) {

            /*
             * Convert Date
             */
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            df.setTimeZone(TimeZone.getTimeZone("America/La_Paz"));

            /*
             * Map Products
             * */
            Stream<ProductResponseDto> productResponseDtoStream = orderData.get().getItems().stream().map(item -> {
                return new ProductResponseDto(
                        item.getProductId(), item.getName(), item.getCategory(), item.getUnitPrice(), item.getActive()
                );
            });
            List<ProductResponseDto> productResponseDtoList = productResponseDtoStream.collect(Collectors.toList());

            OrderResponseDto orderResponseDto = new OrderResponseDto(
                    orderData.get().getOrderId(),
                    orderData.get().getOrderNumber(),
                    orderData.get().getStatus(),
                    df.format(orderData.get().getRegisterDate()),
                    orderData.get().getCustomer(),
                    orderData.get().getSubtotal(),
                    orderData.get().getCityTaxAmount(),
                    orderData.get().getCountyTaxAmount(),
                    orderData.get().getStateTaxAmount(),
                    orderData.get().getFederalTaxAmount(),
                    orderData.get().getTotalTaxesAmount(),
                    orderData.get().getTotalAmount(),
                    productResponseDtoList
            );
            return new ResponseEntity<>(orderResponseDto, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    @PostMapping("/orders")
    public ResponseEntity<Order> createOrder(@RequestBody OrderRequestDto orderRequestDto) {

        LOGGER.info("REQUEST: Init Request Post Order");
        try {
            /*
             * Calculate Order Number
             * */
            List<Order> orders = orderRepository.findAll(Sort.by(Sort.Direction.DESC, "orderNumber"));
            Integer orderNumber = null;
            Optional<Order> orderData = orders.stream().findFirst();
            orderNumber = orderData.map(value -> value.getOrderNumber() + 1).orElse(1);

            /*
             * Calculate Amounts
             * */

            BigDecimal subtotal = BigDecimal.ZERO;
            for (ProductRequestDto product : orderRequestDto.getItems()) {
                subtotal = subtotal.add(product.getUnitPrice());
            }

            BigDecimal cityTaxAmount = subtotal.multiply(BigDecimal.valueOf(0.10));
            BigDecimal countyTaxAmount = (subtotal.add(cityTaxAmount)).multiply(BigDecimal.valueOf(0.05));
            BigDecimal stateTaxAmount = (subtotal.add(cityTaxAmount).add(countyTaxAmount)).multiply(BigDecimal.valueOf(0.08));
            BigDecimal federalTaxAmount = (subtotal.add(cityTaxAmount).add(countyTaxAmount).add(stateTaxAmount)).multiply(BigDecimal.valueOf(0.02));

            BigDecimal totalTaxesAmount = cityTaxAmount
                    .add(countyTaxAmount)
                    .add(stateTaxAmount)
                    .add(federalTaxAmount);

            BigDecimal totalAmount = subtotal.add(totalTaxesAmount);

            /*
             * Save Order
             * */

            Order order = new Order(
                    orderNumber,
                    "Pending",
                    new Date(),
                    orderRequestDto.getCustomer(),
                    subtotal,
                    cityTaxAmount,
                    countyTaxAmount,
                    stateTaxAmount,
                    federalTaxAmount,
                    totalTaxesAmount,
                    totalAmount,
                    orderRequestDto.getItems()
            );
            Order _order = orderRepository.save(order);
            LOGGER.info("REQUEST-SUCCESS: Save order successfull");
            return new ResponseEntity<>(_order, HttpStatus.CREATED);
        } catch (Exception e) {
            LOGGER.error("REQUEST-ERROR: Exception {}", e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/orders/{id}")
    public ResponseEntity<Order> updateOrder(@PathVariable("id") String id, @RequestBody UpdateOrderRequestDto updateOrderRequestDto) {
        Optional<Order> orderData = orderRepository.findById(id);

        if (orderData.isPresent()) {
            Order _order = orderData.get();
            _order.setStatus(updateOrderRequestDto.getStatus());
            _order.setItems(updateOrderRequestDto.getItems());
            /*
             * Calculate Amounts
             * */
            BigDecimal subtotal = BigDecimal.ZERO;
            for (ProductRequestDto product : updateOrderRequestDto.getItems()) {
                subtotal = subtotal.add(product.getUnitPrice());
            }

            BigDecimal cityTaxAmount = subtotal.multiply(BigDecimal.valueOf(0.10));
            BigDecimal countyTaxAmount = (subtotal.add(cityTaxAmount)).multiply(BigDecimal.valueOf(0.05));
            BigDecimal stateTaxAmount = (subtotal.add(cityTaxAmount).add(countyTaxAmount)).multiply(BigDecimal.valueOf(0.08));
            BigDecimal federalTaxAmount = (subtotal.add(cityTaxAmount).add(countyTaxAmount).add(stateTaxAmount)).multiply(BigDecimal.valueOf(0.02));

            BigDecimal totalTaxesAmount = cityTaxAmount
                    .add(countyTaxAmount)
                    .add(stateTaxAmount)
                    .add(federalTaxAmount);

            BigDecimal totalAmount = subtotal.add(totalTaxesAmount);

            /*
             * Set Data
             * */
            _order.setSubtotal(subtotal);
            _order.setCityTaxAmount(cityTaxAmount);
            _order.setCountyTaxAmount(countyTaxAmount);
            _order.setStateTaxAmount(stateTaxAmount);
            _order.setFederalTaxAmount(federalTaxAmount);
            _order.setTotalTaxesAmount(totalTaxesAmount);
            _order.setTotalAmount(totalAmount);
            return new ResponseEntity<>(orderRepository.save(_order), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    @DeleteMapping("/orders/{id}")
    public ResponseEntity<HttpStatus> deleteOrder(@PathVariable("id") String id) {
        try {
            orderRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
