package com.heymart.coupon.service;

import com.heymart.coupon.dto.CouponRequest;
import com.heymart.coupon.model.TransactionCoupon;
import com.heymart.coupon.model.builder.TransactionCouponBuilder;
import com.heymart.coupon.repository.TransactionCouponRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TransactionCouponServiceImpl implements CouponService{

    @Autowired
    private TransactionCouponRepository transactionCouponRepository;

    public TransactionCoupon createCoupon(CouponRequest request) {
        TransactionCoupon coupon = new TransactionCouponBuilder()
                .setPercentDiscount(request.getPercentDiscount())
                .setFixedDiscount(request.getFixedDiscount())
                .setMaxDiscount(request.getMaxDiscount())
                .setSupermarketName(request.getSupermarketName())
                .setMinTransaction(request.getMinTransaction())
                .build();
        return transactionCouponRepository.save(coupon);
    }

    public TransactionCoupon updateCoupon(CouponRequest request) {
        Optional<TransactionCoupon>optional = transactionCouponRepository.findById(request.getId());
        TransactionCoupon coupon = optional.orElseThrow(() -> new RuntimeException("Coupon not found"));
        coupon.setPercentDiscount(request.getPercentDiscount());
        coupon.setFixedDiscount(request.getFixedDiscount());
        coupon.setMaxDiscount(request.getMaxDiscount());
        coupon.setMinTransaction(request.getMinTransaction());
        return transactionCouponRepository.save(coupon);
    }

    public void deleteCoupon(CouponRequest request) {
        Optional<TransactionCoupon>optional = transactionCouponRepository.findById(request.getId());
        TransactionCoupon coupon = optional.orElseThrow(() -> new RuntimeException("Coupon not found"));
        transactionCouponRepository.delete(coupon);
    }

    public List<TransactionCoupon> findAllCoupons() {
        return transactionCouponRepository.findAll();
    }
}
