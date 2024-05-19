package com.heymart.coupon.service.coupon;

import com.heymart.coupon.dto.CouponRequest;
import com.heymart.coupon.model.Coupon;
import com.heymart.coupon.model.ProductCoupon;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public interface ProductCouponOperation {
    public ProductCoupon findByIdProduct(String idProduct);
}
