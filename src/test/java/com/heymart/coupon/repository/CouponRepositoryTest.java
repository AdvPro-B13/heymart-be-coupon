package com.heymart.coupon.repository;

import com.heymart.coupon.model.Coupon;
import com.heymart.coupon.model.builder.CouponBuilder;
import com.heymart.coupon.model.enums.CouponType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class CouponRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CouponRepository couponRepository;

    @Test
    public void testSaveCoupon() {
        Coupon coupon = new Coupon();
        coupon.setId("123");
        coupon.setType("Product");
        coupon.setPercentDiscount(10);
        coupon.setFixedDiscount(5);
        coupon.setMaxDiscount(50);
        coupon.setSupermarket("LocalMart");
        coupon.setIdProduct("P001");
        coupon.setMinTransaction(0);

        Coupon savedCoupon = couponRepository.save(coupon);
        assertNotNull(savedCoupon);
        assertEquals("Product", savedCoupon.getType());
    }
}
