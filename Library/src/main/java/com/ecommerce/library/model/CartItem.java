package com.ecommerce.library.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "cart_items")
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "shopping_cart_id", referencedColumnName = "shopping_cart_id")
    @ToString.Exclude
    private Cart cart;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id", referencedColumnName = "product_id")
    @ToString.Exclude
    private Product product;

    private double unitPrice;

    //Minimum one quantity Required
    @Min(value = 1)
    private int quantity;

//===============Change I made on sunday 22/10/2023=========================
    public double getTotalPrice() {
        if (product != null) {
            return unitPrice * quantity;
        } else {
            return 0.0; // Handle the case where the product is not set
        }
    }
//===============================================================
}
