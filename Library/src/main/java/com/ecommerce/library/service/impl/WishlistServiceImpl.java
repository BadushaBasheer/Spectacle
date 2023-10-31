package com.ecommerce.library.service.impl;

import com.ecommerce.library.model.Customer;
import com.ecommerce.library.model.Product;
import com.ecommerce.library.model.Wishlist;
import com.ecommerce.library.repository.WishlistRepository;
import com.ecommerce.library.service.ProductService;
import com.ecommerce.library.service.WishlistService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WishlistServiceImpl implements WishlistService {
    private final WishlistRepository wishlistRepository;

    private final ProductService productService;

    public WishlistServiceImpl(WishlistRepository wishlistRepository, ProductService productService) {
        this.wishlistRepository = wishlistRepository;
        this.productService = productService;
    }

    @Override
    public List<Wishlist> findAllByCustomer(Customer customer) {
        List<Wishlist>Wishlists=wishlistRepository.findAllByCustomerUsername(customer.getUsername());
        return Wishlists;
    }

    @Override
    public boolean findByProductId(long productId, Customer customer) {
        boolean exists= wishlistRepository.findByProductIdAndCustomerId(productId,customer.getId());
        return exists;
    }

    @Override
    public Wishlist save(long productId, Customer customer) {
        Product product=productService.findById(productId);
        Wishlist wishlist=new Wishlist();
        wishlist.setProduct(product);
        wishlist.setCustomer(customer);
        wishlist.setWishlistName(customer.getUsername());
        wishlistRepository.save(wishlist);
        return wishlist;
    }

    @Override
    public void deleteWishlist(long id) {
        Wishlist wishlist=wishlistRepository.findById(id);
        wishlistRepository.delete(wishlist);
    }

    @Override
    public Wishlist findById(long id) {
        return wishlistRepository.findById(id);
    }

    @Override
    public void createWishlist(String wishlistName, Customer customer) {

        Wishlist existingWishlist = wishlistRepository.findByCustomerAndWishlistName(customer, wishlistName);

        if (existingWishlist != null) {
            throw new IllegalArgumentException("A wishlist with the same name has already exists.");
        }

        Wishlist wishlist = new Wishlist();
        wishlist.setWishlistName(wishlistName);
        wishlist.setCustomer(customer);
        wishlistRepository.save(wishlist);
    }
}
