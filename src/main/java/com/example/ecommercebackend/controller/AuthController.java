package com.example.ecommercebackend.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.ecommercebackend.dto.RegisterRequest;
import com.example.ecommercebackend.model.Customer;
import com.example.ecommercebackend.model.Role;
import com.example.ecommercebackend.repository.CustomerRepository;
import com.example.ecommercebackend.util.JwtUtil;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest request) {
        Customer customer = new Customer();
        customer.setName(request.getName());
        customer.setEmail(request.getEmail());
        customer.setPhone(request.getPhone());
        customer.setPassword(passwordEncoder.encode(request.getPassword()));
        customer.setRole(Role.CUSTOMER);
        customerRepository.save(customer);
        return ResponseEntity.ok("Customer Registered Successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody Map<String, String> request) {
        try {
            String email = request.get("email");
            String password = request.get("password");
            
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
            
            Customer customer = customerRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));

            String token = jwtUtil.generateToken(customer.getEmail(), customer.getRole());
        
            return ResponseEntity.ok(token);
        }
        catch(AuthenticationException e) {
            return ResponseEntity.status(401).body("Invalid Credentials");
        }
    }
}
