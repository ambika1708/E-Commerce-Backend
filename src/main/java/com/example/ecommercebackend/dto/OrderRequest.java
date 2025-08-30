package com.example.ecommercebackend.dto;

import com.example.ecommercebackend.model.PaymentMethod;

public class OrderRequest {
    private PaymentMethod paymentMethod;
    private String shippingAddress;
    private String couponCode;
    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }
    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
    public String getShippingAddress() {
        return shippingAddress;
    }
    public void setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
    }
    public String getCouponCode() {
        return couponCode;
    }
    public void setCouponCode(String couponCode) {
        this.couponCode = couponCode;
    }
}
