package com.example.ecommercebackend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.ecommercebackend.dto.CartItemRequest;
import com.example.ecommercebackend.dto.CartResponse;
import com.example.ecommercebackend.service.CartService;

@RestController
@RequestMapping("/api/cart")
public class CartController {
    @Autowired
    private CartService cartService;

    @PostMapping("/{customerId}/add")
    public ResponseEntity<CartResponse> addToCart(@PathVariable Long customerId, @RequestBody CartItemRequest request) {
        return ResponseEntity.ok(cartService.addToCart(customerId, request));
    }

    @GetMapping("/{customerId}")
    public ResponseEntity<CartResponse> viewCart(@PathVariable Long customerId) {
        return ResponseEntity.ok(cartService.viewCart(customerId));
    }

    @PutMapping("/{customerId}/update")
    public ResponseEntity<CartResponse> updateCart(@PathVariable Long customerId, @RequestBody CartItemRequest request) {
        return ResponseEntity.ok(cartService.updateCart(customerId, request));
    }

    @DeleteMapping("/{customerId}/remove/{productId}")
    public ResponseEntity<CartResponse> removeFromCart(@PathVariable Long customerId, @PathVariable Long productId) {
        return ResponseEntity.ok(cartService.removeFromCart(customerId, productId));
    }
}