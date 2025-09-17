package com.example.ecommercebackend.dto;

import com.example.ecommercebackend.model.PaymentMethod;

public class OrderRequest {
    private PaymentMethod paymentMethod;
    private String shippingAddress;
    private String currency;
    
    public String getShippingAddress() {
        return shippingAddress;
    }
    public void setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
    }
    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }
    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
    public String getCurrency() {
        return currency;
    }
    public void setCurrency(String currency) {
        this.currency = currency;
    }
}
