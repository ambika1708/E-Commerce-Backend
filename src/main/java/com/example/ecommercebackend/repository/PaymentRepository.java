package com.example.ecommercebackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.ecommercebackend.model.Order;
import com.example.ecommercebackend.model.Payment;


public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Payment findByOrder(Order order);
}
