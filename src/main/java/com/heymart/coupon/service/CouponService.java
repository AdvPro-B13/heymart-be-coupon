package com.heymart.coupon.service;

import com.heymart.coupon.model.Coupon;

import java.util.List;

public interface CouponService {
    public Coupon createCoupon(Coupon coupon);
    public Coupon edit(Coupon editedCoupon);
    public Coupon delete(Coupon editedCoupon);
    public List<Coupon> findAll();
}
