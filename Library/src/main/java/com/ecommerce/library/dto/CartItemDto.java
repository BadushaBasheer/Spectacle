package com.ecommerce.library.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class CartItemDto {
    private Long id;

    private ShoppingCartDto cart;

    private ProductDto product;

    @NotBlank
    @NotEmpty
    private int quantity;

    @NotBlank
    @NotEmpty
    private double unitPrice;


}