package com.heymart.coupon.service;

import com.heymart.coupon.dto.CouponRequest;
import com.heymart.coupon.model.ProductCoupon;
import com.heymart.coupon.model.TransactionCoupon;
import com.heymart.coupon.model.builder.ProductCouponBuilder;
import com.heymart.coupon.model.builder.TransactionCouponBuilder;
import com.heymart.coupon.repository.ProductCouponRepository;
import com.heymart.coupon.repository.TransactionCouponRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TransactionCouponServiceImpl implements CouponService{

    @Autowired
    private ProductCouponRepository productCouponRepository;

    @Autowired
    private TransactionCouponRepository transactionCouponRepository;

    public ProductCoupon createProductCoupon(CouponRequest request) {
        ProductCoupon coupon = new ProductCouponBuilder()
                .setPercentDiscount(request.getPercentDiscount())
                .setFixedDiscount(request.getFixedDiscount())
                .setMaxDiscount(request.getMaxDiscount())
                .setSupermarketName(request.getSupermarketName())
                .setIdProduct(request.getIdProduct())
                .build();
        return productCouponRepository.save(coupon);
    }

    public ProductCoupon updateProductCoupon(CouponRequest request) {
        Optional<ProductCoupon>optional = productCouponRepository.findById(request.getId());
        ProductCoupon coupon = optional.orElseThrow(() -> new RuntimeException("Coupon not found"));
        coupon.setPercentDiscount(request.getPercentDiscount());
        coupon.setFixedDiscount(request.getFixedDiscount());
        coupon.setMaxDiscount(request.getMaxDiscount());
        return productCouponRepository.save(coupon);
    }

    public TransactionCoupon createTransactionCoupon(CouponRequest request) {
        TransactionCoupon coupon = new TransactionCouponBuilder()
                .setPercentDiscount(request.getPercentDiscount())
                .setFixedDiscount(request.getFixedDiscount())
                .setMaxDiscount(request.getMaxDiscount())
                .setSupermarketName(request.getSupermarketName())
                .setMinTransaction(request.getMinTransaction())
                .build();
        return transactionCouponRepository.save(coupon);
    }

    public TransactionCoupon updateTransactionCoupon(CouponRequest request) {
        Optional<TransactionCoupon>optional = transactionCouponRepository.findById(request.getId());
        TransactionCoupon coupon = optional.orElseThrow(() -> new RuntimeException("Coupon not found"));
        coupon.setPercentDiscount(request.getPercentDiscount());
        coupon.setFixedDiscount(request.getFixedDiscount());
        coupon.setMaxDiscount(request.getMaxDiscount());
        coupon.setMinTransaction(request.getMinTransaction());
        return transactionCouponRepository.save(coupon);
    }

    public void deleteTransactionCoupon(CouponRequest request) {
        Optional<TransactionCoupon>optional = transactionCouponRepository.findById(request.getId());
        TransactionCoupon coupon = optional.orElseThrow(() -> new RuntimeException("Coupon not found"));
        transactionCouponRepository.delete(coupon);
    }

    public void deleteProductCoupon(CouponRequest request) {
        Optional<ProductCoupon>optional = productCouponRepository.findById(request.getId());
        ProductCoupon coupon = optional.orElseThrow(() -> new RuntimeException("Coupon not found"));
        productCouponRepository.delete(coupon);
    }

    public List<ProductCoupon> findAllProductCoupons() {
        return productCouponRepository.findAll();
    }

    public List<TransactionCoupon> findAllTransactionCoupons() {
        return transactionCouponRepository.findAll();
    }
}
