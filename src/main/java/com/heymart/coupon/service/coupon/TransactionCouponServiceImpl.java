package com.heymart.coupon.service.coupon;

import com.heymart.coupon.dto.CouponRequest;
import com.heymart.coupon.model.ProductCoupon;
import com.heymart.coupon.model.TransactionCoupon;
import com.heymart.coupon.model.builder.TransactionCouponBuilder;
import com.heymart.coupon.repository.CouponRepository;
import com.heymart.coupon.repository.TransactionCouponRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Service
@Qualifier("transactionCouponService")
@EnableAsync
public class TransactionCouponServiceImpl implements CouponService<TransactionCoupon>{

    @Autowired
    private TransactionCouponRepository couponRepository;

    public TransactionCoupon createCoupon(CouponRequest request) {
        TransactionCoupon coupon = new TransactionCouponBuilder()
                .setPercentDiscount(request.getPercentDiscount())
                .setFixedDiscount(request.getFixedDiscount())
                .setMaxDiscount(request.getMaxDiscount())
                .setSupermarketName(request.getSupermarketName())
                .setMinTransaction(request.getMinTransaction())
                .build();
        return couponRepository.save(coupon);
    }

    public TransactionCoupon updateCoupon(CouponRequest request) {
        Optional<TransactionCoupon>optional = couponRepository.findById(request.getId());
        TransactionCoupon coupon = optional.orElseThrow(() -> new RuntimeException("Coupon not found"));
        coupon.setPercentDiscount(request.getPercentDiscount());
        coupon.setFixedDiscount(request.getFixedDiscount());
        coupon.setMaxDiscount(request.getMaxDiscount());
        coupon.setMinTransaction(request.getMinTransaction());
        return couponRepository.save(coupon);
    }

    public void deleteCoupon(CouponRequest request) {
        Optional<TransactionCoupon>optional = couponRepository.findById(request.getId());
        TransactionCoupon coupon = optional.orElseThrow(() -> new RuntimeException("Coupon not found"));
        couponRepository.delete(coupon);
    }

    public CompletableFuture<List<TransactionCoupon>> findAllCoupons() {
        return CompletableFuture.supplyAsync(() -> {
            return couponRepository.findAll();
        });
    }

    @Override
    public TransactionCoupon findById(String id) {
        Optional<TransactionCoupon>optional = couponRepository.findById(id);
        return optional.orElseThrow(() -> new RuntimeException("Coupon not found"));
    }

    @Override
    public List<TransactionCoupon> findBySupermarketName(String supermarketName) {
        return couponRepository.findBySupermarketName(supermarketName);
    }
}
