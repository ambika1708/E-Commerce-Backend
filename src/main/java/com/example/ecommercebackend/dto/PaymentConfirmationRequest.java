package com.example.ecommercebackend.dto;

import com.example.ecommercebackend.model.PaymentStatus;

public class PaymentConfirmationRequest {
    private Long paymentId;
    private PaymentStatus paymentStatus;
    public Long getPaymentId() {
        return paymentId;
    }
    public void setPaymentId(Long paymentId) {
        this.paymentId = paymentId;
    }
    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }
    public void setPaymentStatus(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }
}
