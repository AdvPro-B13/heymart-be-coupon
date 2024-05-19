package com.heymart.coupon.service.coupon;

import com.heymart.coupon.dto.CouponRequest;
import com.heymart.coupon.model.ProductCoupon;
import com.heymart.coupon.model.builder.ProductCouponBuilder;
import com.heymart.coupon.repository.ProductCouponRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Service
@EnableAsync
public class ProductCouponServiceImpl implements CouponService<ProductCoupon>, ProductCouponOperation {

    private final ProductCouponRepository couponRepository;
    @Autowired
    public ProductCouponServiceImpl(ProductCouponRepository couponRepository) {
        this.couponRepository = couponRepository;
    }

    @Async("asyncTaskExecutor")
    public CompletableFuture<ProductCoupon> createCoupon(CouponRequest request) {
        ProductCoupon coupon = new ProductCouponBuilder()
                .setPercentDiscount(request.getPercentDiscount())
                .setFixedDiscount(request.getFixedDiscount())
                .setMaxDiscount(request.getMaxDiscount())
                .setSupermarketName(request.getSupermarketName())
                .setIdProduct(request.getIdProduct())
                .build();
        return CompletableFuture.completedFuture(couponRepository.save(coupon));
    }

    @Async("asyncTaskExecutor")
    public CompletableFuture<ProductCoupon> updateCoupon(CouponRequest request) {
        Optional<ProductCoupon> optional = couponRepository.findById(request.getId());
        ProductCoupon coupon = optional.orElseThrow(() -> new RuntimeException("Coupon not found"));
        coupon.setPercentDiscount(request.getPercentDiscount());
        coupon.setFixedDiscount(request.getFixedDiscount());
        coupon.setMaxDiscount(request.getMaxDiscount());
        return CompletableFuture.completedFuture(couponRepository.save(coupon));
    }

    @Async("asyncTaskExecutor")
    public CompletableFuture<Void> deleteCoupon(CouponRequest request) {
        Optional<ProductCoupon> optional = couponRepository.findById(request.getId());
        ProductCoupon coupon = optional.orElseThrow(() -> new RuntimeException("Coupon not found"));
        couponRepository.delete(coupon);
        return CompletableFuture.completedFuture(null);
    }

    public List<ProductCoupon> findAllCoupons() {
        System.out.println(Thread.currentThread().getName());
        return couponRepository.findAll();
    }

    public ProductCoupon findById(String id) {
        Optional<ProductCoupon>optional = couponRepository.findById(id);
        return optional.orElseThrow(() -> new RuntimeException("Coupon not found"));
    }

    public List<ProductCoupon> findBySupermarketName(String supermarketName) {
        return couponRepository.findBySupermarketName(supermarketName);
    }
    public ProductCoupon findByIdProduct(String idProduct) {
        ProductCoupon coupon = couponRepository.findByIdProduct(idProduct);
        if (coupon == null) {
            throw new RuntimeException("Coupon not found");
        }
        return coupon;
    }
}
