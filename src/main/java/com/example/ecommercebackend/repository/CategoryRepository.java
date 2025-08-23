package com.example.ecommercebackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.ecommercebackend.model.Category;

public interface  CategoryRepository extends JpaRepository<Category, Long>{
    
}
