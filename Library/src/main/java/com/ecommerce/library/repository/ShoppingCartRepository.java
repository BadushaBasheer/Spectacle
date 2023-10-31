package com.ecommerce.library.repository;

import com.ecommerce.library.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShoppingCartRepository extends JpaRepository<Cart, Long> {
}
