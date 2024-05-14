package com.heymart.coupon.repository;

import com.heymart.coupon.model.ProductCoupon;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductCouponRepository extends CouponRepository<ProductCoupon> {
    List<ProductCoupon> findBySupermarketName(String supermarketName);
    ProductCoupon findByIdProduct(String idProduct);
}
