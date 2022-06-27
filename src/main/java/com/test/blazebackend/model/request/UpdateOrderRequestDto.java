package com.test.blazebackend.model.request;

import java.util.List;

public class UpdateOrderRequestDto {

    private String status;
    private List<ProductOrderRequestDto> items;

    public UpdateOrderRequestDto() {

    }

    public UpdateOrderRequestDto(String status, List<ProductOrderRequestDto> items) {
        this.status = status;
        this.items = items;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<ProductOrderRequestDto> getItems() {
        return items;
    }

    public void setItems(List<ProductOrderRequestDto> items) {
        this.items = items;
    }

    @Override
    public String toString() {
        return "UpdateOrderRequestDto{" +
                "status='" + status + '\'' +
                ", items=" + items +
                '}';
    }
}
