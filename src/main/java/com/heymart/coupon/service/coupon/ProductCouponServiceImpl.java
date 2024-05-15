package com.heymart.coupon.service.coupon;

import com.heymart.coupon.dto.CouponRequest;
import com.heymart.coupon.model.ProductCoupon;
import com.heymart.coupon.model.builder.ProductCouponBuilder;
import com.heymart.coupon.repository.CouponRepository;
import com.heymart.coupon.repository.ProductCouponRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Service
@Qualifier("productCouponService")
@EnableAsync
public class ProductCouponServiceImpl implements CouponService<ProductCoupon> {

    @Autowired
    private ProductCouponRepository couponRepository;

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

    @Async
    public CompletableFuture<List<ProductCoupon>> findAllCoupons() {
        CompletableFuture<List<ProductCoupon> > future = new CompletableFuture<>();
        List<ProductCoupon> couponList = couponRepository.findAll();
        future.complete(couponList);
        return future;
    }

    public ProductCoupon findById(String id) {
        Optional<ProductCoupon>optional = couponRepository.findById(id);
        return optional.orElseThrow(() -> new RuntimeException("Coupon not found"));
    }

    @Override
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
