package com.test.blazebackend.dao.entity;

import com.test.blazebackend.model.request.ProductOrderRequestDto;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Document(collection = "T_ORDER")
public class Order {
    @Id
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
    private List<ProductOrderRequestDto> items;

    public Order() {
    }

    public Order(Integer orderNumber, String status, Date registerDate, String customer, BigDecimal subtotal, BigDecimal cityTaxAmount, BigDecimal countyTaxAmount, BigDecimal stateTaxAmount, BigDecimal federalTaxAmount, BigDecimal totalTaxesAmount, BigDecimal totalAmount, List<ProductOrderRequestDto> items) {
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

    public List<ProductOrderRequestDto> getItems() {
        return items;
    }

    public void setItems(List<ProductOrderRequestDto> items) {
        this.items = items;
    }

    @Override
    public String toString() {
        return "Order{" +
                "orderId='" + orderId + '\'' +
                ", orderNumber='" + orderNumber + '\'' +
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
