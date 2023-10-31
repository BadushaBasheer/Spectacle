package com.ecommerce.library.service;

import com.ecommerce.library.dto.ProductDto;
import com.ecommerce.library.dto.ShoppingCartDto;
import com.ecommerce.library.model.*;
import jakarta.transaction.Transactional;

import javax.naming.InsufficientResourcesException;

public interface CartService {

    Cart addItemToCart(ProductDto productDto, int quantity, String username);

    Cart updateCart(ProductDto productDto, int quantity, String username);

    Cart removeItemFromCart(ProductDto productDto, String username);

    Cart updateTotalPrice(Double newTotalPrice, String username);

    @Transactional
    void deleteCartById(Long id);


//    ShoppingCartDto addItemToCartSession(ShoppingCartDto cartDto, ProductDto productDto, int quantity);
//
//    ShoppingCartDto updateCartSession(ShoppingCartDto cartDto, ProductDto productDto, int quantity);
//
//    ShoppingCartDto removeItemFromCartSession(ShoppingCartDto cartDto, ProductDto productDto, int quantity);

//    Cart combineCart(ShoppingCartDto cartDto, Cart cart);
//
//
//    void deleteCartById(Long id);
//
//    Cart getCart(String username);
//
//    Cart updateTotalPrice(Double newTotalPrice,String username);


}
