package com.heymart.coupon.service.coupon;

import com.heymart.coupon.model.ProductCoupon;
import org.springframework.stereotype.Service;

@Service
public interface ProductCouponOperation {
    public ProductCoupon findByIdProduct(String idProduct);
}
