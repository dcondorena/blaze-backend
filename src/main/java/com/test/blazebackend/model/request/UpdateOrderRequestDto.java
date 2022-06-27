package com.test.blazebackend.model.request;

import java.util.List;

public class UpdateOrderRequestDto {

    private String status;
    private List<ProductRequestDto> items;

    public UpdateOrderRequestDto() {

    }

    public UpdateOrderRequestDto(String status, List<ProductRequestDto> items) {
        this.status = status;
        this.items = items;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<ProductRequestDto> getItems() {
        return items;
    }

    public void setItems(List<ProductRequestDto> items) {
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
