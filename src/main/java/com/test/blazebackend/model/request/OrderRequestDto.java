package com.test.blazebackend.model.request;
import java.util.List;

public class OrderRequestDto {

    private String customer;
    private List<ProductOrderRequestDto> items;

    public OrderRequestDto(){

    }

    public OrderRequestDto(String customer, List<ProductOrderRequestDto> items) {
        this.customer = customer;
        this.items = items;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public List<ProductOrderRequestDto> getItems() {
        return items;
    }

    public void setItems(List<ProductOrderRequestDto> items) {
        this.items = items;
    }
}
