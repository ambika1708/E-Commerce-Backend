package com.example.ecommercebackend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.ecommercebackend.model.Customer;
import com.example.ecommercebackend.model.Order;


public interface OrderRepository extends JpaRepository<Order, Long>{
    List<Order> findByCustomer(Customer customer);
}
