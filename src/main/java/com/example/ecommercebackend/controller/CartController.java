package com.example.ecommercebackend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.ecommercebackend.dto.CartItemRequest;
import com.example.ecommercebackend.model.Cart;
import com.example.ecommercebackend.service.CartService;

@RestController
@RequestMapping("/api/cart")
public class CartController {
    @Autowired
    private CartService cartService;

    @PostMapping("/{customerId}/add")
    public ResponseEntity<Cart> addToCart(@PathVariable Long customerId, @RequestBody CartItemRequest request) {
        return ResponseEntity.ok(cartService.addToCart(customerId, request));
    }
}