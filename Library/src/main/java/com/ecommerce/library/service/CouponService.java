package com.ecommerce.library.service;

import com.ecommerce.library.dto.CouponDto;
import com.ecommerce.library.model.Coupon;

import java.util.List;

public interface CouponService {

    Coupon save(CouponDto couponDto);

    List<CouponDto> getAllCoupons();

    double applyCoupon(String couponCode,double totalPrice);

    boolean findByCouponCode(String couponCode);

    Coupon findByCode(String couponCode);

    CouponDto findById(long id);

    Coupon update(CouponDto couponDto);

    void enable(long id);

    void disable(long id);

    void deleteCoupon(long id);
}
