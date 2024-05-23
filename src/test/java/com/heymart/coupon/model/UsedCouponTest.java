package com.heymart.coupon.model;
import com.heymart.coupon.model.Coupon;
import com.heymart.coupon.model.UsedCoupon;
import com.heymart.coupon.model.builder.TransactionCouponBuilder;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UsedCouponTest {

    @Test
    public void testUsedCouponConstructorAndGetters() {
        TransactionCoupon coupon = new TransactionCouponBuilder()
                .setPercentDiscount(10)
                .setFixedDiscount(5)
                .setMaxDiscount(20)
                .setSupermarketId("Supermarket")
                .setMinTransaction(50)
                .build();
        Long userId = 1L;
        String supermarketId = "SUPERMARKET123";

        UsedCoupon usedCoupon = new UsedCoupon(coupon, userId, supermarketId);
        assertNull(usedCoupon.getId());
        assertNotNull(usedCoupon);
        assertEquals(coupon, usedCoupon.getCoupon());
        assertEquals(userId, usedCoupon.getUserId());
        assertEquals(supermarketId, usedCoupon.getSupermarketId());
    }

    @Test
    public void testUsedCouponNoArgsConstructor() {
        UsedCoupon usedCoupon = new UsedCoupon();
        assertNotNull(usedCoupon);
    }
}
