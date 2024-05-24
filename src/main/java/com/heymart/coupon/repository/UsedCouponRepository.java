package com.heymart.coupon.repository;

import com.heymart.coupon.model.UsedCoupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;


@Repository
public interface UsedCouponRepository extends JpaRepository<UsedCoupon, Long> {
    boolean existsByUserIdAndCouponId(Long userId, UUID couponId);
    void  deleteByCouponId(UUID couponId);
}
