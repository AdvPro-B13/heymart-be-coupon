package com.heymart.coupon.service;

import com.heymart.coupon.dto.CouponRequest;
import com.heymart.coupon.model.Coupon;
import com.heymart.coupon.model.ProductCoupon;
import com.heymart.coupon.model.TransactionCoupon;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public interface CouponService<T extends Coupon> {
    public T createCoupon(CouponRequest request);
    public T updateCoupon(CouponRequest request);
    public void deleteCoupon(CouponRequest request);
    public List<T> findAllCoupons();
}
