package com.example.ecommercebackend.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.ecommercebackend.dto.CartItemRequest;
import com.example.ecommercebackend.dto.CartProductResponse;
import com.example.ecommercebackend.dto.CartResponse;
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

    public CartResponse addToCart(Long customerId, CartItemRequest request) {
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
        cartRepository.save(cart); 
        
        List<CartProductResponse> cartProductResponses = cart.getItems().stream()
                                                                        .map(item -> new CartProductResponse(item.getProduct().getId(),
                                                                                                            item.getProduct().getName(),
                                                                                                            item.getQuantity(),
                                                                                                            item.getProduct().getPrice())).toList(); 

        CartResponse response = new CartResponse(cart.getId(),cart.getTotalPrice(),cartProductResponses);

        return response;
    }

    public CartResponse viewCart(Long customerId) {
        Customer customer = customerRepository.findById(customerId)
                                              .orElseThrow(() -> new RuntimeException("Customer Not Found"));

        Cart cart = customer.getCart();
        List<CartProductResponse> cartProductResponses = cart.getItems().stream()
                                                                        .map(item -> new CartProductResponse(item.getProduct().getId(),
                                                                                                            item.getProduct().getName(),
                                                                                                            item.getQuantity(),
                                                                                                            item.getProduct().getPrice())).toList(); 

        CartResponse response = new CartResponse(cart.getId(),cart.getTotalPrice(),cartProductResponses);

        return response;
    }

    public CartResponse removeCart(Long customerId, Long productId) {
        Customer customer = customerRepository.findById(customerId)
                                              .orElseThrow(() -> new RuntimeException("Customer Not Found"));
        
        Cart cart = customer.getCart();
        if(cart==null) {
            throw new RuntimeException("Cart is Empty");
        }
        cart.getItems().removeIf(item -> item.getProduct().getId().equals(productId));
        cart.setTotalPrice(cart.getItems().stream()
                                          .mapToDouble(CartItem::getPrice)
                                          .sum());

        cartRepository.save(cart);
        List<CartProductResponse> cartProductResponses = cart.getItems().stream()
                                                                        .map(item -> new CartProductResponse(item.getProduct().getId(),
                                                                                                            item.getProduct().getName(),
                                                                                                            item.getQuantity(),
                                                                                                            item.getProduct().getPrice())).toList(); 

        CartResponse response = new CartResponse(cart.getId(),cart.getTotalPrice(),cartProductResponses);

        return response;
    }
    
}
