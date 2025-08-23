package com.example.ecommercebackend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.ecommercebackend.model.Product;

@Repository
public interface  ProductRepository extends JpaRepository<Product, Long>{
    List<Product> findByCategoryId(Long categoryId);
}
