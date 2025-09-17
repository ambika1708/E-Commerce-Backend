package com.example.ecommercebackend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.ecommercebackend.repository.CustomerRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService{
    @Autowired
    private CustomerRepository customerRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
       return customerRepository.findByEmail(username)
                    .map(customer -> User.builder()
                                         .username(customer.getEmail())
                                         .password(customer.getPassword())
                                         .roles(customer.getRole().name())
                                         .build())
                    .orElseThrow(() -> new UsernameNotFoundException("User Not Found"));
    }
}
