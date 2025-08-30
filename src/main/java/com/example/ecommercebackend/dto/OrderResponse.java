package com.example.ecommercebackend.dto;

import java.time.LocalDateTime;
import java.util.List;

public class OrderResponse {
    private Long orderId;
    private LocalDateTime orderDate;
    private double price;
    private String status;
    private List<OrderItemResponse> item;
    private PaymentResponse payment;
    public Long getOrderId() {
        return orderId;
    }
    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }
    public LocalDateTime getOrderDate() {
        return orderDate;
    }
    public void setOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
    }
    public double getPrice() {
        return price;
    }
    public void setPrice(double price) {
        this.price = price;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public List<OrderItemResponse> getItem() {
        return item;
    }
    public void setItem(List<OrderItemResponse> item) {
        this.item = item;
    }
    public PaymentResponse getPayment() {
        return payment;
    }
    public void setPayment(PaymentResponse payment) {
        this.payment = payment;
    }
    
    
}
