package com.heymart.coupon.repository;

import com.heymart.coupon.model.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.stereotype.Repository;

@Repository
public interface CouponRepository<T extends Coupon> extends JpaRepository<T, String> {
}
