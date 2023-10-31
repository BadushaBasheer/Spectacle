package com.ecommerce.library.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@Entity
@ToString
@Table(name = "shopping_cart")
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "shopping_cart_id")
    private Long id;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "customer_id", referencedColumnName = "customer_id")
    @ToString.Exclude
    private Customer customer;

    private double totalPrice;

    private int  totalItems;


    @OneToMany(cascade = CascadeType.ALL, mappedBy = "cart")
    @ToString.Exclude
    private Set<CartItem> cartItems = new HashSet<>();
    public Cart() {
        this.totalItems = 0;
        this.totalPrice = 0.0;
    }


}