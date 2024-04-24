package com.heymart.coupon.repository;

import com.heymart.coupon.model.Coupon;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
public interface CouponRepository extends JpaRepository<Coupon, String>{

}
