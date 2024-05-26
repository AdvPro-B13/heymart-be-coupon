package com.heymart.coupon.model;
import com.heymart.coupon.model.builder.TransactionCouponBuilder;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class UsedCouponTest {

    @Test
    void testUsedCouponConstructorAndGetters() {

        Long userId = 1L;
        TransactionCoupon coupon = new TransactionCouponBuilder()
                .setPercentDiscount(10)
                .setFixedDiscount(5)
                .setMaxDiscount(20)
                .setSupermarketId("Supermarket")
                .setMinTransaction(50)
                .build();

        UUID couponId =  UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
        UsedCoupon usedCoupon = new UsedCoupon(couponId,coupon.getSupermarketId(), userId);
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
