package com.heymart.coupon.service.coupon;

import com.heymart.coupon.dto.CouponRequest;
import com.heymart.coupon.repository.UsedCouponRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
@EnableAsync
public class UsedCouponServiceImpl implements UsedCouponService {
    private final UsedCouponRepository usedCouponRepository;
    @Autowired
    public UsedCouponServiceImpl(UsedCouponRepository usedCouponRepository) {
        this.usedCouponRepository = usedCouponRepository;
    }
    @Async("asyncTaskExecutor")
    public CompletableFuture<Void>  deleteUsedCouponsByCouponId(CouponRequest request) {
        UUID couponId = UUID.fromString(request.getId());
        usedCouponRepository.deleteByCouponId(couponId);
        return CompletableFuture.completedFuture(null);
    }
}
