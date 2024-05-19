package com.heymart.coupon.service.coupon;

import com.heymart.coupon.dto.CouponRequest;
import com.heymart.coupon.model.Coupon;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public interface CouponService<T extends Coupon> {
    public CompletableFuture<T> createCoupon(CouponRequest request);
    public CompletableFuture<T> updateCoupon(CouponRequest request);
    public CompletableFuture<Void> deleteCoupon(CouponRequest request);
    public List<T> findAllCoupons();
    public T findById(String id);
    public List<T> findBySupermarketName(String supermarketName);
}
