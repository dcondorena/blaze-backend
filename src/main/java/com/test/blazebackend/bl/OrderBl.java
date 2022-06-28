package com.test.blazebackend.bl;

import com.test.blazebackend.dao.entity.Order;
import com.test.blazebackend.dao.entity.ProductOrder;
import com.test.blazebackend.dao.repository.OrderRepository;
import com.test.blazebackend.exception.ServiceException;
import com.test.blazebackend.model.request.OrderRequestDto;
import com.test.blazebackend.model.request.ProductOrderRequestDto;
import com.test.blazebackend.model.request.UpdateOrderRequestDto;
import com.test.blazebackend.model.response.OrderResponseDto;
import com.test.blazebackend.model.response.ProductOrderResponseDto;
import com.test.blazebackend.model.response.ProductResponseDto;
import com.test.blazebackend.util.MapperUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class OrderBl {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductBl.class);
    private final OrderRepository orderRepository;
    private final MapperUtil mapperUtil;

    @Autowired
    public OrderBl(OrderRepository orderRepository, MapperUtil mapperUtil) {
        this.orderRepository = orderRepository;
        this.mapperUtil = mapperUtil;
    }

    public Map<String, Object> getOrderPage(
            Integer orderNumber, int page, int size) {
        LOGGER.info("ORDER-BL: Starting paginated product request");
        try {
            Pageable paging = PageRequest.of(page, size);
            Page<Order> orderPage;

            if (orderNumber == null) {
                LOGGER.info("QUERY-DB: Consult the database to obtain a list of orders");
                orderPage = orderRepository.findAll(paging);
                LOGGER.info("SUCCESS-QUERY-DB: OrderPage: " + orderPage);
            } else {
                LOGGER.info("QUERY-DB: Consult the database to obtain a list of orders by orderNumber");
                orderPage = orderRepository.findByOrderNumberContainingIgnoreCase(orderNumber, paging);
                LOGGER.info("SUCCESS-QUERY-DB: OrderPage By OrderNumber: " + orderPage);
            }

            Stream<OrderResponseDto> orderResponseDtoStream = mapperUtil.mapOrderResponseDto(orderPage.getContent().stream());

            List<OrderResponseDto> orders = orderResponseDtoStream.collect(Collectors.toList());


            Map<String, Object> response = new HashMap<>();
            response.put("orders", orders);
            response.put("currentPage", orderPage.getNumber());
            response.put("totalItems", orderPage.getTotalElements());
            response.put("totalPages", orderPage.getTotalPages());

            LOGGER.debug("ORDER-BL-SUCCES: Response:{} ", response);
            LOGGER.info("ORDER-BL-SUCCESS: Paginated order request successfull");
            return response;
        } catch (Exception e) {
            LOGGER.error("ORDER-BL-ERROR: Error:{} ", e.getMessage());
            throw new ServiceException(500, "Paginated Order Request Failed");
        }
    }

    public OrderResponseDto getOrderDetail(String id) {
        LOGGER.info("ORDER-BL: Starting request to get order detail");

        LOGGER.info("QUERY-DB: Starting find order");
        Optional<Order> orderData = orderRepository.findById(id);
        LOGGER.info("QUERY-DB-SUCCESS: Find order successfull");

        if (orderData.isPresent()) {
            /*
             * Convert Date
             */
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            df.setTimeZone(TimeZone.getTimeZone("America/La_Paz"));

            /*
             * Map Products
             * */
            Stream<ProductOrderResponseDto> productOrderResponseDtoStream = orderData.get().getItems().stream().map(item -> {
                return new ProductOrderResponseDto(
                        item.getProductId(), item.getName(), item.getCategory(), item.getUnitPrice(), item.getQuantity(),
                        item.getCost(), item.getActive()
                );
            });
            List<ProductOrderResponseDto> productOrderResponseDtoList = productOrderResponseDtoStream.collect(Collectors.toList());

            OrderResponseDto orderResponseDto = new OrderResponseDto(
                    orderData.get().getOrderId(),
                    orderData.get().getOrderNumber(),
                    orderData.get().getStatus(),
                    df.format(orderData.get().getRegisterDate()),
                    orderData.get().getCustomer(),
                    orderData.get().getSubtotal().setScale(2, RoundingMode.HALF_EVEN),
                    orderData.get().getCityTaxAmount().setScale(2, RoundingMode.HALF_EVEN),
                    orderData.get().getCountyTaxAmount().setScale(2, RoundingMode.HALF_EVEN),
                    orderData.get().getStateTaxAmount().setScale(2, RoundingMode.HALF_EVEN),
                    orderData.get().getFederalTaxAmount().setScale(2, RoundingMode.HALF_EVEN),
                    orderData.get().getTotalTaxesAmount().setScale(2, RoundingMode.HALF_EVEN),
                    orderData.get().getTotalAmount().setScale(2, RoundingMode.HALF_EVEN),
                    productOrderResponseDtoList
            );
            LOGGER.info("ORDER-BL: Request to get order detail successfull");
            return orderResponseDto;
        } else {
            throw new ServiceException(404, "Order Detail Not Found");
        }
    }


    public Order createOrder(OrderRequestDto orderRequestDto) {
        LOGGER.info("ORDER-BL: Starting order creation  request");
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
            LOGGER.info("ORDER-BL: Starting calculating amounts");
            BigDecimal subtotal = BigDecimal.ZERO;

            List<ProductOrder> productOrders = new ArrayList<>();
            for (ProductOrderRequestDto product : orderRequestDto.getItems()) {
                BigDecimal cost = product.getUnitPrice().multiply(BigDecimal.valueOf(product.getQuantity()));
                subtotal = subtotal.add(cost);
                productOrders.add(new ProductOrder(
                        product.getProductId(), product.getName(), product.getCategory(), product.getUnitPrice(),
                        product.getQuantity(), cost,
                        product.getActive()
                ));
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
            LOGGER.info("ORDER-BL: Calculating amounts successfull");
            /*
             * Save Order
             * */

            Order order = new Order(
                    orderNumber,
                    "Pending",
                    new Date(),
                    orderRequestDto.getCustomer(),
                    subtotal.setScale(2, RoundingMode.HALF_EVEN),
                    cityTaxAmount.setScale(2, RoundingMode.HALF_EVEN),
                    countyTaxAmount.setScale(2, RoundingMode.HALF_EVEN),
                    stateTaxAmount.setScale(2, RoundingMode.HALF_EVEN),
                    federalTaxAmount.setScale(2, RoundingMode.HALF_EVEN),
                    totalTaxesAmount.setScale(2, RoundingMode.HALF_EVEN),
                    totalAmount.setScale(2, RoundingMode.HALF_EVEN),
                    productOrders
            );

            LOGGER.info("QUERY-DB: Starting save order");
            Order _order = orderRepository.save(order);
            LOGGER.info("QUERY-DB-SUCCESS: Save order successfull");
            return _order;
        } catch (Exception e) {
            LOGGER.error("REQUEST-ERROR: Exception {}", e.getMessage());
            throw new ServiceException(500, "Order Creation Failed");
        }
    }

    public Order updateOrder(String id, UpdateOrderRequestDto updateOrderRequestDto) {
        LOGGER.info("ORDER-BL: Starting update order request");

        LOGGER.info("QUERY-DB: Starting find order");
        Optional<Order> orderData = orderRepository.findById(id);
        LOGGER.info("QUERY-DB-SUCCESS: Find order successfull");

        if (orderData.isPresent()) {
            Order _order = orderData.get();
            _order.setStatus(updateOrderRequestDto.getStatus());
            /*
             * Calculate Amounts
             * */
            BigDecimal subtotal = BigDecimal.ZERO;
            /*
             * Map ProductOrder
             */
            List<ProductOrder> productOrders = new ArrayList<>();
            for (ProductOrderRequestDto product : updateOrderRequestDto.getItems()) {
                BigDecimal cost = product.getUnitPrice().multiply(BigDecimal.valueOf(product.getQuantity()));
                subtotal = subtotal.add(cost);
                productOrders.add(new ProductOrder(
                        product.getProductId(), product.getName(), product.getCategory(), product.getUnitPrice(),
                        product.getQuantity(), cost,
                        product.getActive()
                ));
            }
            _order.setItems(productOrders);

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
            _order.setSubtotal(subtotal.setScale(2, RoundingMode.HALF_EVEN));
            _order.setCityTaxAmount(cityTaxAmount.setScale(2, RoundingMode.HALF_EVEN));
            _order.setCountyTaxAmount(countyTaxAmount.setScale(2, RoundingMode.HALF_EVEN));
            _order.setStateTaxAmount(stateTaxAmount.setScale(2, RoundingMode.HALF_EVEN));
            _order.setFederalTaxAmount(federalTaxAmount.setScale(2, RoundingMode.HALF_EVEN));
            _order.setTotalTaxesAmount(totalTaxesAmount.setScale(2, RoundingMode.HALF_EVEN));
            _order.setTotalAmount(totalAmount.setScale(2, RoundingMode.HALF_EVEN));


            Order response = orderRepository.save(_order);
            LOGGER.info("ORDER-BL: Update order request successfull");
            return response;
        } else {
            throw new ServiceException(404, "Order Not Found");
        }
    }

    public void deleteOrder(String id) {
        try {
            LOGGER.info("QUERY-DB: Initiating query to delete order");
            orderRepository.deleteById(id);
            LOGGER.info("QUERY-DB: Initiating query to delete order");
        } catch (Exception e) {
            LOGGER.error("ORDER-BL-ERROR: Error:{} ", e.getMessage());
            throw new ServiceException(500, "Deleted Order Failed");
        }
    }

}
