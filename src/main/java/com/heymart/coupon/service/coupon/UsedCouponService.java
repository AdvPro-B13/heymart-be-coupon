package com.heymart.coupon.service.coupon;

import com.heymart.coupon.dto.CouponRequest;
import com.heymart.coupon.model.TransactionCoupon;
import com.heymart.coupon.model.UsedCoupon;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public interface UsedCouponService {
    public CompletableFuture<Void> deleteUsedCouponsByCouponId(CouponRequest request);
    public UsedCoupon useCoupon(TransactionCoupon coupon, Long userId);
    public List<UsedCoupon> getUsedCouponBySupermarket(String supermarketId, Long userId);
}
