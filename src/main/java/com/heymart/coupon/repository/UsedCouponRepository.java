package com.heymart.coupon.repository;

import com.heymart.coupon.model.Coupon;
import com.heymart.coupon.model.UsedCoupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface UsedCouponRepository extends JpaRepository<UsedCoupon, Long> {
    boolean existsByUserIdAndCoupon(Long userId, Coupon coupon);
    List<UsedCoupon> findBySupermarketId(String supermarketId);
}
