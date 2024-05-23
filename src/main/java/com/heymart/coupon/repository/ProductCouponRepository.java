package com.heymart.coupon.repository;

import com.heymart.coupon.model.ProductCoupon;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductCouponRepository extends CouponRepository<ProductCoupon> {

    ProductCoupon findByIdProduct(String idProduct);
}
