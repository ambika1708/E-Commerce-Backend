package com.example.ecommercebackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.ecommercebackend.model.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long>{
    
}
