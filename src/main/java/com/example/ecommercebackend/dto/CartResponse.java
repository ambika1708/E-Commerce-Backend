package com.example.ecommercebackend.dto;

import java.util.List;

public class CartResponse {
    private Long cartId;
    private Double totalPrice;
    private List<CartProductResponse> items;
    
    public CartResponse(Long cartId, Double totalPrice, List<CartProductResponse> items) {
        this.cartId = cartId;
        this.totalPrice = totalPrice;
        this.items = items;
    }
    public Long getCartId() {
        return cartId;
    }
    public void setCartId(Long cartId) {
        this.cartId = cartId;
    }
    public Double getTotalPrice() {
        return totalPrice;
    }
    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }
    public List<CartProductResponse> getItems() {
        return items;
    }
    public void setItems(List<CartProductResponse> items) {
        this.items = items;
    }
    
}
