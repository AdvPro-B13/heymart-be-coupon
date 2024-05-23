package com.heymart.coupon.service.coupon;

import com.heymart.coupon.dto.CouponRequest;
import com.heymart.coupon.enums.ErrorStatus;
import com.heymart.coupon.exception.CouponNotFoundException;
import com.heymart.coupon.model.ProductCoupon;
import com.heymart.coupon.model.builder.ProductCouponBuilder;
import com.heymart.coupon.repository.ProductCouponRepository;
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
public class ProductCouponServiceImpl implements CouponService<ProductCoupon>, ProductCouponOperation {

    private final ProductCouponRepository couponRepository;
    @Autowired
    public ProductCouponServiceImpl(ProductCouponRepository couponRepository) {
        this.couponRepository = couponRepository;
    }

    public ProductCoupon createCoupon(CouponRequest request) {
        ProductCoupon coupon = new ProductCouponBuilder()
                .setPercentDiscount(request.getPercentDiscount())
                .setFixedDiscount(request.getFixedDiscount())
                .setMaxDiscount(request.getMaxDiscount())
                .setSupermarketId(request.getSupermarketId())
                .setIdProduct(request.getIdProduct())
                .build();
        return couponRepository.save(coupon);
    }

    public ProductCoupon updateCoupon(CouponRequest request) {
        Optional<ProductCoupon> optional = couponRepository.findById(UUID.fromString(request.getId()));
        ProductCoupon coupon = optional.orElseThrow(() -> new CouponNotFoundException(ErrorStatus.COUPON_NOT_FOUND.getValue()));
        coupon.setPercentDiscount(request.getPercentDiscount());
        coupon.setFixedDiscount(request.getFixedDiscount());
        coupon.setMaxDiscount(request.getMaxDiscount());
        return couponRepository.save(coupon);
    }

    @Async("asyncTaskExecutor")
    public CompletableFuture<Void> deleteCoupon(CouponRequest request) {
        Optional<ProductCoupon> optional = couponRepository.findById(UUID.fromString(request.getId()));
        ProductCoupon coupon = optional.orElseThrow(() -> new CouponNotFoundException(ErrorStatus.COUPON_NOT_FOUND.getValue()));
        couponRepository.delete(coupon);
        return CompletableFuture.completedFuture(null);
    }

    public List<ProductCoupon> findAllCoupons() {
        return couponRepository.findAll();
    }

    public ProductCoupon findById(String id) {
        Optional<ProductCoupon>optional = couponRepository.findById(UUID.fromString(id));
        return optional.orElseThrow(() -> new CouponNotFoundException(ErrorStatus.COUPON_NOT_FOUND.getValue()));
    }

    public List<ProductCoupon> findBySupermarketId(String supermarketId) {
        return couponRepository.findBySupermarketId(supermarketId);
    }
    public ProductCoupon findByIdProduct(String idProduct) {
        ProductCoupon coupon = couponRepository.findByIdProduct(idProduct);
        if (coupon == null) {
            throw new CouponNotFoundException(ErrorStatus.COUPON_NOT_FOUND.getValue());
        }
        return coupon;
    }
}
