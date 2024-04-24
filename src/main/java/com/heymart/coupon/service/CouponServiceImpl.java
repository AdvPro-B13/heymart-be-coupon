package com.heymart.coupon.service;

import com.heymart.coupon.model.Coupon;
import com.heymart.coupon.repository.CouponRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CouponServiceImpl {
    @Autowired
    private CouponRepository couponRepository;
    public Coupon createCoupon(Coupon coupon) {
        couponRepository.save(coupon);
        return coupon;
    }
    public Coupon edit(Coupon editedCoupon) {
        couponRepository.save(editedCoupon);
        return editedCoupon;
    }
    public Coupon delete(Coupon editedCoupon) {
        couponRepository.delete(editedCoupon);
        return editedCoupon;
    }
    public List<Coupon> findAll() {
        return couponRepository.findAll();
    }
}
