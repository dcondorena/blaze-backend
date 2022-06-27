package com.test.blazebackend.model.request;

import java.math.BigDecimal;

public class ProductRequestDto {

    private String productId;
    private String name;
    private String category;
    private BigDecimal unitPrice;
    private Boolean active;

    public ProductRequestDto() {

    }

    public ProductRequestDto(String productId, String name, String category, BigDecimal unitPrice, Boolean active) {
        this.productId = productId;
        this.name = name;
        this.category = category;
        this.unitPrice = unitPrice;
        this.active = active;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }


    @Override
    public String toString() {
        return "ProductRequestDto{" +
                "productId='" + productId + '\'' +
                ", name='" + name + '\'' +
                ", category='" + category + '\'' +
                ", unitPrice=" + unitPrice +
                ", active=" + active +
                '}';
    }
}
