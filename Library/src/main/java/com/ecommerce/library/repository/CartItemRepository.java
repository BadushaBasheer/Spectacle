package com.ecommerce.library.repository;

import com.ecommerce.library.model.CartItem;
import com.ecommerce.library.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem,Long> {
    CartItem findByProduct(Product product);

}
