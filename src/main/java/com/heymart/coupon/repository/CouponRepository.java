package com.heymart.coupon.repository;

import com.heymart.coupon.model.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface CouponRepository<T extends Coupon> extends JpaRepository<T, String> {
}
