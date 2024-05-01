package com.heymart.coupon.service;

import com.heymart.coupon.model.ProductCoupon;
import com.heymart.coupon.model.TransactionCoupon;
import com.heymart.coupon.repository.ProductCouponRepository;
import com.heymart.coupon.repository.TransactionCouponRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CouponServiceImpl implements CouponService{

    @Autowired
    private ProductCouponRepository productCouponRepository;

    @Autowired
    private TransactionCouponRepository transactionCouponRepository;

    public ProductCoupon createOrUpdateProductCoupon(ProductCoupon coupon) {
        return productCouponRepository.save(coupon);
    }

    public TransactionCoupon createOrUpdateTransactionCoupon(TransactionCoupon coupon) {
        return transactionCouponRepository.save(coupon);
    }

    public void deleteTransactionCoupon(TransactionCoupon coupon) {
        transactionCouponRepository.delete(coupon);
    }

    public void deleteProductCoupon(ProductCoupon coupon) {
        productCouponRepository.delete(coupon);
    }

    public List<ProductCoupon> findAllProductCoupons() {
        return productCouponRepository.findAll();
    }

    public List<TransactionCoupon> findAllTransactionCoupons() {
        return transactionCouponRepository.findAll();
    }

    // Additional methods to handle business logic
}
