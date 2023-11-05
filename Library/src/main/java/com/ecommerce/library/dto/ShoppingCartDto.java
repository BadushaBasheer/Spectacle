package com.ecommerce.library.dto;

import com.ecommerce.library.model.Customer;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.*;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ShoppingCartDto {


    private Long id;

    private Customer customer;

    @NotBlank
    @NotEmpty
    private double totalPrice;

    @NotBlank
    @NotEmpty
    private int totalItems;

    private Set<CartItemDto> cartItems;


}
