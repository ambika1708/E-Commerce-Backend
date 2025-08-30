package com.example.ecommercebackend.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.example.ecommercebackend.model.Customer;
import com.example.ecommercebackend.repository.CustomerRepository;

@Component
public class AuthUtil {
    private final CustomerRepository customerRepository;

    public AuthUtil(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public Customer getLoggedInCustomer() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return customerRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("Customer Not Found"));
    }
}
