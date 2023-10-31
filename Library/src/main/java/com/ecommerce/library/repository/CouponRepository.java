package com.ecommerce.library.repository;

import com.ecommerce.library.model.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CouponRepository extends JpaRepository<Coupon,Long> {
    Coupon findCouponByCode(String code);

    Coupon findById(long id);
}