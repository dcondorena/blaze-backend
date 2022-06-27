package com.test.blazebackend.model.response;

import com.test.blazebackend.dao.entity.Product;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class OrderResponseDto {

    private String orderId;
    private Integer orderNumber;
    private String status;
    private Date registerDate;
    private String customer;
    private BigDecimal subtotal;
    private BigDecimal cityTaxAmount;
    private BigDecimal countyTaxAmount;
    private BigDecimal stateTaxAmount;
    private BigDecimal federalTaxAmount;
    private BigDecimal totalTaxesAmount;
    private BigDecimal totalAmount;
    private List<ProductResponseDto> items;

    public OrderResponseDto() {
    }

    public OrderResponseDto(String orderId, Integer orderNumber, String status, Date registerDate, String customer, BigDecimal subtotal, BigDecimal cityTaxAmount, BigDecimal countyTaxAmount, BigDecimal stateTaxAmount, BigDecimal federalTaxAmount, BigDecimal totalTaxesAmount, BigDecimal totalAmount, List<ProductResponseDto> items) {
        this.orderId = orderId;
        this.orderNumber = orderNumber;
        this.status = status;
        this.registerDate = registerDate;
        this.customer = customer;
        this.subtotal = subtotal;
        this.cityTaxAmount = cityTaxAmount;
        this.countyTaxAmount = countyTaxAmount;
        this.stateTaxAmount = stateTaxAmount;
        this.federalTaxAmount = federalTaxAmount;
        this.totalTaxesAmount = totalTaxesAmount;
        this.totalAmount = totalAmount;
        this.items = items;
    }


    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public Integer getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(Integer orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getRegisterDate() {
        return registerDate;
    }

    public void setRegisterDate(Date registerDate) {
        this.registerDate = registerDate;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    public BigDecimal getCityTaxAmount() {
        return cityTaxAmount;
    }

    public void setCityTaxAmount(BigDecimal cityTaxAmount) {
        this.cityTaxAmount = cityTaxAmount;
    }

    public BigDecimal getCountyTaxAmount() {
        return countyTaxAmount;
    }

    public void setCountyTaxAmount(BigDecimal countyTaxAmount) {
        this.countyTaxAmount = countyTaxAmount;
    }

    public BigDecimal getStateTaxAmount() {
        return stateTaxAmount;
    }

    public void setStateTaxAmount(BigDecimal stateTaxAmount) {
        this.stateTaxAmount = stateTaxAmount;
    }

    public BigDecimal getFederalTaxAmount() {
        return federalTaxAmount;
    }

    public void setFederalTaxAmount(BigDecimal federalTaxAmount) {
        this.federalTaxAmount = federalTaxAmount;
    }

    public BigDecimal getTotalTaxesAmount() {
        return totalTaxesAmount;
    }

    public void setTotalTaxesAmount(BigDecimal totalTaxesAmount) {
        this.totalTaxesAmount = totalTaxesAmount;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public List<ProductResponseDto> getItems() {
        return items;
    }

    public void setItems(List<ProductResponseDto> items) {
        this.items = items;
    }


    @Override
    public String toString() {
        return "OrderResponseDto{" +
                "orderId='" + orderId + '\'' +
                ", orderNumber=" + orderNumber +
                ", status='" + status + '\'' +
                ", registerDate=" + registerDate +
                ", customer='" + customer + '\'' +
                ", subtotal=" + subtotal +
                ", cityTaxAmount=" + cityTaxAmount +
                ", countyTaxAmount=" + countyTaxAmount +
                ", stateTaxAmount=" + stateTaxAmount +
                ", federalTaxAmount=" + federalTaxAmount +
                ", totalTaxesAmount=" + totalTaxesAmount +
                ", totalAmount=" + totalAmount +
                ", items=" + items +
                '}';
    }
}
