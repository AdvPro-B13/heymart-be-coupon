package com.heymart.coupon.model;
import com.heymart.coupon.model.builder.TransactionCouponBuilder;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class UsedCouponTest {

    @Test
    void testUsedCouponConstructorAndGetters() {

        Long userId = 1L;
        UUID couponId =  UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
        UsedCoupon usedCoupon = new UsedCoupon(couponId, userId);
        assertNull(usedCoupon.getId());
        assertNotNull(usedCoupon);
        assertEquals(couponId, usedCoupon.getCouponId());
        assertEquals(userId, usedCoupon.getUserId());
    }

    @Test
    void testUsedCouponNoArgsConstructor() {
        UsedCoupon usedCoupon = new UsedCoupon();
        assertNotNull(usedCoupon);
    }
}
