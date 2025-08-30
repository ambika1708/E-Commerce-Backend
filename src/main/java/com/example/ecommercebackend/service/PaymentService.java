package com.example.ecommercebackend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.ecommercebackend.dto.PaymentConfirmationRequest;
import com.example.ecommercebackend.dto.PaymentResponse;
import com.example.ecommercebackend.model.Order;
import com.example.ecommercebackend.model.OrderStatus;
import com.example.ecommercebackend.model.Payment;
import com.example.ecommercebackend.model.PaymentStatus;
import com.example.ecommercebackend.repository.OrderRepository;
import com.example.ecommercebackend.repository.PaymentRepository;


@Service
public class PaymentService {
    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Transactional
    public PaymentResponse confirmPayment(Long orderId, PaymentConfirmationRequest request) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new RuntimeException("Order not found"));

        Payment payment = order.getPayment();
        if(payment==null) {
            throw new RuntimeException("No payment found for this order");
        }
        if(payment.getPaymentStatus()==PaymentStatus.SUCCESS) {
            throw new RuntimeException("Payment Already Done!");
        }
        payment.setPaymentStatus(request.getPaymentStatus());
        paymentRepository.save(payment);

        if(request.getPaymentStatus()==PaymentStatus.SUCCESS) {
            order.setStatus(OrderStatus.CONFIRMED);
        }
        else if(request.getPaymentStatus()==PaymentStatus.FAILED) {
            order.setStatus(OrderStatus.CANCELLED);
        }

        orderRepository.save(order);

        PaymentResponse response = new PaymentResponse();
        response.setPaymentId(payment.getId());
        response.setMethod(payment.getPaymentMethod().toString());
        response.setStatus(payment.getPaymentStatus().toString());
        response.setOrderId(orderId);

        return response;
    }

    public void refundPayment(Order order) {
        Payment payment = paymentRepository.findByOrder(order);
        if(payment.getPaymentStatus()==PaymentStatus.SUCCESS) {
            payment.setPaymentStatus(PaymentStatus.REFUNDED);
            paymentRepository.save(payment);
        }
    }
    
}
