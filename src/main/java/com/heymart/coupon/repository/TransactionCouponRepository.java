package com.heymart.coupon.repository;

import com.heymart.coupon.model.ProductCoupon;
import com.heymart.coupon.model.TransactionCoupon;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionCouponRepository extends CouponRepository<TransactionCoupon> {
    List<TransactionCoupon> findBySupermarketName(String supermarketName);
}
