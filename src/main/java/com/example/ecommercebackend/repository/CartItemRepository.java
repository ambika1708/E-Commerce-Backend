package com.example.ecommercebackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.ecommercebackend.model.CartItem;

public interface CartItemRepository extends JpaRepository<CartItem, Long>{
    
}
