package com.heymart.coupon.service.coupon;

import com.heymart.coupon.dto.CouponRequest;
import com.heymart.coupon.dto.UserResponse;
import com.heymart.coupon.enums.ErrorStatus;
import com.heymart.coupon.exception.CouponAlreadyUsedException;
import com.heymart.coupon.model.TransactionCoupon;
import com.heymart.coupon.model.UsedCoupon;
import com.heymart.coupon.repository.UsedCouponRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
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
    public UsedCoupon useCoupon(TransactionCoupon coupon, Long userId){
        boolean isExist = usedCouponRepository.existsByUserIdAndCouponId(userId, coupon.getId());
        if (isExist) {
            throw new CouponAlreadyUsedException(ErrorStatus.COUPON_ALREADY_USED.getValue());
        }
        UsedCoupon usedCoupon = new UsedCoupon(coupon.getId(), coupon.getSupermarketId(), userId);
        return usedCouponRepository.save(usedCoupon);
    }
}
