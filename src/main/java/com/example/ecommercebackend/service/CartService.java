package com.example.ecommercebackend.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.ecommercebackend.dto.CartItemRequest;
import com.example.ecommercebackend.model.Cart;
import com.example.ecommercebackend.model.CartItem;
import com.example.ecommercebackend.model.Customer;
import com.example.ecommercebackend.model.Product;
import com.example.ecommercebackend.repository.CartRepository;
import com.example.ecommercebackend.repository.CustomerRepository;
import com.example.ecommercebackend.repository.ProductRepository;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CustomerRepository customerRepository;

    public Cart addToCart(Long customerId, CartItemRequest request) {
        Customer customer = customerRepository.findById(customerId)
                                              .orElseThrow(() -> new RuntimeException("Customer Not Found"));
        Product product = productRepository.findById(request.getProductId())
                                           .orElseThrow(() -> new RuntimeException("Product Out of Stock"));
        Cart cart = customer.getCart();
        if(cart==null) {
            cart = new Cart();
            cart.setCustomer(customer);
        }

        Optional<CartItem> existingItem = cart.getItems().stream()
                                               .filter(item -> item.getProduct().getId().equals(product.getId()))
                                               .findFirst();
        if(existingItem.isPresent()) {
            CartItem item = existingItem.get();
            item.setQuantity(item.getQuantity() + request.getQuantity());
            item.setPrice(item.getQuantity() * product.getPrice());
        }
        else {
            CartItem cartItem = new CartItem();
            cartItem.setProduct(product);
            cartItem.setCart(cart);
            cartItem.setQuantity(request.getQuantity());
            cartItem.setPrice(cartItem.getQuantity() * product.getPrice());
            cart.getItems().add(cartItem);
        }
        cart.setTotalPrice(cart.getItems().stream()
                                          .mapToDouble(CartItem::getPrice)
                                          .sum());
        return cartRepository.save(cart);                  
    }
    
}
