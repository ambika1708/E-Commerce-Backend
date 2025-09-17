package com.example.ecommercebackend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.ecommercebackend.model.Cart;
import com.example.ecommercebackend.model.Customer;

public interface CartRepository extends JpaRepository<Cart, Long>{
    Optional<Cart> findByCustomer(Customer customer);
}
