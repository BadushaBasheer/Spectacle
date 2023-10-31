package com.ecommerce.library.service;

import com.ecommerce.library.model.Customer;
import com.ecommerce.library.model.Wishlist;

import java.util.List;

public interface WishlistService {

    List<Wishlist> findAllByCustomer(Customer customer);

    boolean findByProductId(long id,Customer customer);

    Wishlist save(long productId,Customer customer);

    void deleteWishlist(long id);

    Wishlist findById(long id);

    void createWishlist(String wishlistName, Customer customer);
}
