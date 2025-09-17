package com.example.ecommercebackend.dto;

public class OrderItemResponse {
    private String prodcutName;
    private int quantity;
    private double price;
    public String getProdcutName() {
        return prodcutName;
    }
    public void setProdcutName(String prodcutName) {
        this.prodcutName = prodcutName;
    }
    public int getQuantity() {
        return quantity;
    }
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    public double getPrice() {
        return price;
    }
    public void setPrice(double price) {
        this.price = price;
    }
    
}
