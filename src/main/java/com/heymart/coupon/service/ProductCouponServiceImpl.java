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
public class ProductCouponServiceImpl implements CouponService {

    @Autowired
    private ProductCouponRepository productCouponRepository;

    public ProductCoupon createCoupon(CouponRequest request) {
        ProductCoupon coupon = new ProductCouponBuilder()
                .setPercentDiscount(request.getPercentDiscount())
                .setFixedDiscount(request.getFixedDiscount())
                .setMaxDiscount(request.getMaxDiscount())
                .setSupermarketName(request.getSupermarketName())
                .setIdProduct(request.getIdProduct())
                .build();
        return productCouponRepository.save(coupon);
    }

    public ProductCoupon updateCoupon(CouponRequest request) {
        Optional<ProductCoupon>optional = productCouponRepository.findById(request.getId());
        ProductCoupon coupon = optional.orElseThrow(() -> new RuntimeException("Coupon not found"));
        coupon.setPercentDiscount(request.getPercentDiscount());
        coupon.setFixedDiscount(request.getFixedDiscount());
        coupon.setMaxDiscount(request.getMaxDiscount());
        return productCouponRepository.save(coupon);
    }

    public void deleteCoupon(CouponRequest request) {
        Optional<ProductCoupon>optional = productCouponRepository.findById(request.getId());
        ProductCoupon coupon = optional.orElseThrow(() -> new RuntimeException("Coupon not found"));
        productCouponRepository.delete(coupon);
    }

    public List<ProductCoupon> findAllCoupons() {
        return productCouponRepository.findAll();
    }

}
