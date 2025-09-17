package com.example.ecommercebackend.dto;

public class PaymentConfirmationRequest {
    private String stripPaymentMethodId;
    private Long paymentId;
    
    public Long getPaymentId() {
        return paymentId;
    }
    public void setPaymentId(Long paymentId) {
        this.paymentId = paymentId;
    }
    public String getStripPaymentMethodId() {
        return stripPaymentMethodId;
    }
    public void setStripPaymentMethodId(String stripPaymentMethodId) {
        this.stripPaymentMethodId = stripPaymentMethodId;
    }
   
}
