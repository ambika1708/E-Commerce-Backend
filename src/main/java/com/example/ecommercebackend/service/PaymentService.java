package com.example.ecommercebackend.service;

import java.time.LocalDateTime;

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
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentConfirmParams;
import com.stripe.param.PaymentIntentCreateParams;


@Service
public class PaymentService {
    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private OrderRepository orderRepository;

    public String createPaymentIntent(Order order, String currency) throws StripeException {
        PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                                                                    .setAmount((long) (order.getTotalPrice().longValue()) * 100) //stripe uses paisa
                                                                    .setCurrency(currency)
                                                                    .setAutomaticPaymentMethods(
                                                                        PaymentIntentCreateParams.AutomaticPaymentMethods.builder()
                                                                                        .setEnabled(true)
                                                                                        .setAllowRedirects(PaymentIntentCreateParams.AutomaticPaymentMethods
                                                                                            .AllowRedirects.NEVER).build())
                                                                    .build();   
        PaymentIntent intent = PaymentIntent.create(params);
        Payment payment = order.getPayment();
        payment.setStripePaymentIntentId(intent.getId());
        payment.setAmount(order.getTotalPrice());
        payment.setCurrency(currency);
        payment.setPaymentDate(LocalDateTime.now());
        paymentRepository.save(payment);

        return intent.getClientSecret(); // normally sent to frontend
    }

    @Transactional
    public PaymentResponse confirmPayment(Long orderId, PaymentConfirmationRequest request) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new RuntimeException("Order not found"));

        Payment payment = order.getPayment();
        if(payment==null) {
            throw new RuntimeException("No payment found for this order");
        }

        try {
            PaymentIntent intent = PaymentIntent.retrieve(payment.getStripePaymentIntentId());
            PaymentIntentConfirmParams confirmParams = PaymentIntentConfirmParams.builder().setPaymentMethod(request.getStripPaymentMethodId()).build();
            intent = intent.confirm(confirmParams);
            if("succeeded".equals(intent.getStatus())) {
                payment.setPaymentStatus(PaymentStatus.SUCCESS);
                order.setStatus(OrderStatus.CONFIRMED);
            }
            else {
                payment.setPaymentStatus(PaymentStatus.FAILED);
                payment.setFailureReason(intent.getLastPaymentError()!=null?intent.getLastPaymentError().getMessage():"Payment Failed");
                order.setStatus(OrderStatus.CANCELLED);
            }

        } catch(StripeException ex) {
            payment.setPaymentStatus(PaymentStatus.FAILED);
            payment.setFailureReason(ex.getMessage());
            order.setStatus(OrderStatus.CANCELLED);
            throw new RuntimeException("Stripe failed payment: "+ex.getMessage());
        }
        
        paymentRepository.save(payment);
        orderRepository.save(order);

        PaymentResponse response = new PaymentResponse();
        response.setPaymentId(payment.getId());
        response.setMethod(payment.getPaymentMethod().toString());
        response.setStatus(payment.getPaymentStatus().toString());
        response.setOrderId(orderId);
        response.setAmount(payment.getAmount());
        response.setCurrency(payment.getCurrency());
        response.setFailureReason(payment.getFailureReason());
        response.setPaymentDate(payment.getPaymentDate());
        response.setStripeChargeId(payment.getStripePaymentIntentId());

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
