package com.ecommerce.customer.config;

import com.ecommerce.library.model.Customer;
import com.ecommerce.library.repository.CustomerRepository;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Locale;
import java.util.stream.Collectors;

public class CustomerServiceConfig implements UserDetailsService {


    private final CustomerRepository customerRepository;

    public CustomerServiceConfig(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Customer customer = customerRepository.findByUsername(username);
        if (customer == null) {
            System.out.println(username);
            throw new UsernameNotFoundException("Could not find username");
        }
        if (customer.is_blocked()) {
            throw new LockedException("User is blocked");
        }
        return new User(customer.getUsername(),
                customer.getPassword(),
                customer.getRoles().
                        stream().
                        map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList()));
    }
}
