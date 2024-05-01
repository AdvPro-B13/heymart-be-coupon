package com.heymart.coupon.service;

import com.heymart.coupon.model.Coupon;
import com.heymart.coupon.model.ProductCoupon;
import com.heymart.coupon.model.TransactionCoupon;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public interface CouponService {
    public ProductCoupon createOrUpdateProductCoupon(ProductCoupon coupon);
    public TransactionCoupon createOrUpdateTransactionCoupon(TransactionCoupon editedCoupon);
    public void deleteProductCoupon(ProductCoupon coupon);
    public void deleteTransactionCoupon(TransactionCoupon coupon);
    public List<ProductCoupon> findAllProductCoupons();
    public List<TransactionCoupon> findAllTransactionCoupons();
}
