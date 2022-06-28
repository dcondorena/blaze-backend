package com.test.blazebackend.util;

import com.test.blazebackend.dao.entity.Order;
import com.test.blazebackend.dao.entity.Product;
import com.test.blazebackend.model.response.OrderResponseDto;
import com.test.blazebackend.model.response.ProductOrderResponseDto;
import com.test.blazebackend.model.response.ProductResponseDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.TimeZone;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class MapperUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(MapperUtil.class);

    @Autowired
    public MapperUtil() {

    }

    public Stream<ProductResponseDto> mapProductResponseDto(Stream<Product> data) {
        LOGGER.info("MAPPER-UTIL: Starting mapping of ProductResponseDto");
        /*
         * Map Products
         * */
        Stream<ProductResponseDto> dtoStream = data.map(product -> {
            return new ProductResponseDto(
                    product.getProductId(),
                    product.getName(),
                    product.getCategory(),
                    product.getUnitPrice(),
                    product.getActive()
            );
        });
        LOGGER.info("MAPPER-UTIL: Mapping of ProductResponseDto successfull");
        return dtoStream;
    }


    public Stream<OrderResponseDto> mapOrderResponseDto(Stream<Order> data) {
        LOGGER.info("MAPPER-UTIL: Starting mapping of OrderResponseDto");
        /*
         * Map Products
         * */
        Stream<OrderResponseDto> dtoStream = data.map(order -> {
            /*
             * Convert Date
             */
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            df.setTimeZone(TimeZone.getTimeZone("America/La_Paz"));
            df.format(order.getRegisterDate());
            /*
             * Map Products
             * */
            Stream<ProductOrderResponseDto> productResponseDtoStream = order.getItems().stream().map(item -> {
                return new ProductOrderResponseDto(
                        item.getProductId(), item.getName(), item.getCategory(), item.getUnitPrice(), item.getQuantity(),
                        item.getCost(), item.getActive()
                );
            });
            List<ProductOrderResponseDto> productOrderResponseDtoList = productResponseDtoStream.collect(Collectors.toList());
            /*
             * Map Orders
             * */
            return new OrderResponseDto(
                    order.getOrderId(),
                    order.getOrderNumber(),
                    order.getStatus(),
                    df.format(order.getRegisterDate()),
                    order.getCustomer(),
                    order.getSubtotal().setScale(2, RoundingMode.HALF_EVEN),
                    order.getCityTaxAmount().setScale(2, RoundingMode.HALF_EVEN),
                    order.getCountyTaxAmount().setScale(2, RoundingMode.HALF_EVEN),
                    order.getStateTaxAmount().setScale(2, RoundingMode.HALF_EVEN),
                    order.getFederalTaxAmount().setScale(2, RoundingMode.HALF_EVEN),
                    order.getTotalTaxesAmount().setScale(2, RoundingMode.HALF_EVEN),
                    order.getTotalAmount().setScale(2, RoundingMode.HALF_EVEN),
                    productOrderResponseDtoList
            );
        });
        LOGGER.info("MAPPER-UTIL: Mapping of OrderResponseDto successfull");
        return dtoStream;
    }

}
