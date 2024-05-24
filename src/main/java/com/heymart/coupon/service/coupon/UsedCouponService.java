package com.heymart.coupon.service.coupon;

import com.heymart.coupon.dto.CouponRequest;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public interface UsedCouponService {
    public CompletableFuture<Void> deleteUsedCouponsByCouponId(CouponRequest request);
}
