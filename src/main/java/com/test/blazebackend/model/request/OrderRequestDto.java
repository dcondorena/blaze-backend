package com.test.blazebackend.model.request;
import java.util.List;

public class OrderRequestDto {

    private String customer;
    private List<ProductRequestDto> items;

    public OrderRequestDto(){

    }

    public OrderRequestDto(String customer, List<ProductRequestDto> items) {
        this.customer = customer;
        this.items = items;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public List<ProductRequestDto> getItems() {
        return items;
    }

    public void setItems(List<ProductRequestDto> items) {
        this.items = items;
    }
}
