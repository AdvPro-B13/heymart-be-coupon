package com.heymart.coupon.service.coupon;

import com.heymart.coupon.dto.CouponRequest;
import com.heymart.coupon.model.ProductCoupon;
import com.heymart.coupon.model.builder.ProductCouponBuilder;
import com.heymart.coupon.repository.CouponRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Qualifier("productCouponService")
public class ProductCouponServiceImpl implements CouponService<ProductCoupon> {

    @Autowired
    private CouponRepository<ProductCoupon> couponRepository;

    public ProductCoupon createCoupon(CouponRequest request) {
        ProductCoupon coupon = new ProductCouponBuilder()
                .setPercentDiscount(request.getPercentDiscount())
                .setFixedDiscount(request.getFixedDiscount())
                .setMaxDiscount(request.getMaxDiscount())
                .setSupermarketName(request.getSupermarketName())
                .setIdProduct(request.getIdProduct())
                .build();
        return couponRepository.save(coupon);
    }

    public ProductCoupon updateCoupon(CouponRequest request) {
        Optional<ProductCoupon>optional = couponRepository.findById(request.getId());
        ProductCoupon coupon = optional.orElseThrow(() -> new RuntimeException("Coupon not found"));
        coupon.setPercentDiscount(request.getPercentDiscount());
        coupon.setFixedDiscount(request.getFixedDiscount());
        coupon.setMaxDiscount(request.getMaxDiscount());
        return couponRepository.save(coupon);
    }

    public void deleteCoupon(CouponRequest request) {
        Optional<ProductCoupon>optional = couponRepository.findById(request.getId());
        ProductCoupon coupon = optional.orElseThrow(() -> new RuntimeException("Coupon not found"));
        couponRepository.delete(coupon);
    }

    public List<ProductCoupon> findAllCoupons() {
        return couponRepository.findAll();
    }

    public ProductCoupon findById(String id) {
        Optional<ProductCoupon>optional = couponRepository.findById(id);
        return optional.orElseThrow(() -> new RuntimeException("Coupon not found"));
    }

}
