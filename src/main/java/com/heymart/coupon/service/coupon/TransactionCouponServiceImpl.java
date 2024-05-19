package com.heymart.coupon.service.coupon;

import com.heymart.coupon.dto.CouponRequest;
import com.heymart.coupon.enums.ErrorStatus;
import com.heymart.coupon.exception.CouponNotFoundException;
import com.heymart.coupon.model.TransactionCoupon;
import com.heymart.coupon.model.builder.TransactionCouponBuilder;
import com.heymart.coupon.repository.TransactionCouponRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
@EnableAsync
public class TransactionCouponServiceImpl implements CouponService<TransactionCoupon>{

    private final TransactionCouponRepository couponRepository;
    @Autowired
    public TransactionCouponServiceImpl(TransactionCouponRepository couponRepository) {
        this.couponRepository = couponRepository;
    }
    @Async("asyncTaskExecutor")
    public CompletableFuture<TransactionCoupon> createCoupon(CouponRequest request) {
        TransactionCoupon coupon = new TransactionCouponBuilder()
                .setPercentDiscount(request.getPercentDiscount())
                .setFixedDiscount(request.getFixedDiscount())
                .setMaxDiscount(request.getMaxDiscount())
                .setSupermarketName(request.getSupermarketName())
                .setMinTransaction(request.getMinTransaction())
                .build();
        return CompletableFuture.completedFuture(couponRepository.save(coupon));
    }

    @Async("asyncTaskExecutor")
    public CompletableFuture<TransactionCoupon> updateCoupon(CouponRequest request) {
        Optional<TransactionCoupon> optional = couponRepository.findById(UUID.fromString(request.getId()));
        TransactionCoupon coupon = optional.orElseThrow(() -> new CouponNotFoundException(ErrorStatus.COUPON_NOT_FOUND.getValue()));
        coupon.setPercentDiscount(request.getPercentDiscount());
        coupon.setFixedDiscount(request.getFixedDiscount());
        coupon.setMaxDiscount(request.getMaxDiscount());
        coupon.setMinTransaction(request.getMinTransaction());
        return CompletableFuture.completedFuture(couponRepository.save(coupon));
    }

    @Async("asyncTaskExecutor")
    public CompletableFuture<Void> deleteCoupon(CouponRequest request) {
        Optional<TransactionCoupon> optional = couponRepository.findById(UUID.fromString(request.getId()));
        TransactionCoupon coupon = optional.orElseThrow(() -> new CouponNotFoundException(ErrorStatus.COUPON_NOT_FOUND.getValue()));
        couponRepository.delete(coupon);
        return CompletableFuture.completedFuture(null);
    }

    public List<TransactionCoupon> findAllCoupons() {
        return couponRepository.findAll();
    }

    public TransactionCoupon findById(String id) {
        Optional<TransactionCoupon>optional = couponRepository.findById(UUID.fromString(id));
        return optional.orElseThrow(() -> new CouponNotFoundException(ErrorStatus.COUPON_NOT_FOUND.getValue()));
    }

    public List<TransactionCoupon> findBySupermarketName(String supermarketName) {
        return couponRepository.findBySupermarketName(supermarketName);
    }
}
