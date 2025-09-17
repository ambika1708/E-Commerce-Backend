package com.example.ecommercebackend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.ecommercebackend.dto.PaymentConfirmationRequest;
import com.example.ecommercebackend.dto.PaymentResponse;
import com.example.ecommercebackend.service.PaymentService;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {
    @Autowired
    private PaymentService paymentService;

    @PostMapping("/{orderId}/confirm")
    public ResponseEntity<PaymentResponse> confirmPayment(@PathVariable Long orderId, @RequestBody PaymentConfirmationRequest request) {
        return ResponseEntity.ok(paymentService.confirmPayment(orderId, request));
    }
}
