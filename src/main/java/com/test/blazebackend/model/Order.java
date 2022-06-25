package com.test.blazebackend.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Document(collection = "T_ORDER")
public class Order {
    @Id
    private Integer orderId;
    private String status;
    private Date date;
    private String customer;
    private String taxAmount;
    private String totalTaxAmount;
    private String totalAmount;
    private List<Product> items;

    public Order() {
    }

    public Order(Integer orderId, String status, Date date, String customer, String taxAmount, String totalTaxAmount, String totalAmount, List<Product> items) {
        this.orderId = orderId;
        this.status = status;
        this.date = date;
        this.customer = customer;
        this.taxAmount = taxAmount;
        this.totalTaxAmount = totalTaxAmount;
        this.totalAmount = totalAmount;
        this.items = items;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public String getTaxAmount() {
        return taxAmount;
    }

    public void setTaxAmount(String taxAmount) {
        this.taxAmount = taxAmount;
    }

    public String getTotalTaxAmount() {
        return totalTaxAmount;
    }

    public void setTotalTaxAmount(String totalTaxAmount) {
        this.totalTaxAmount = totalTaxAmount;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public List<Product> getItems() {
        return items;
    }

    public void setItems(List<Product> items) {
        this.items = items;
    }


    @Override
    public String toString() {
        return "Order{" +
                "orderId=" + orderId +
                ", status='" + status + '\'' +
                ", date=" + date +
                ", customer='" + customer + '\'' +
                ", taxAmount='" + taxAmount + '\'' +
                ", totalTaxAmount='" + totalTaxAmount + '\'' +
                ", totalAmount='" + totalAmount + '\'' +
                ", items=" + items +
                '}';
    }
}
