package com.heymart.coupon.model.builder;

import com.heymart.coupon.model.TransactionCoupon;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TransactionCouponBuilderTest {

    @Test
    void buildValidTransactionCoupon() {
        TransactionCouponBuilder builder = new TransactionCouponBuilder()
                .setPercentDiscount(10)
                .setFixedDiscount(5)
                .setMaxDiscount(20)
                .setSupermarketName("Supermarket")
                .setMinTransaction(50);

        TransactionCoupon coupon = builder.build();
        assertNotNull(coupon);
        assertEquals(10, coupon.getPercentDiscount());
        assertEquals(5, coupon.getFixedDiscount());
        assertEquals(20, coupon.getMaxDiscount());
        assertEquals("Supermarket", coupon.getSupermarketName());
        assertEquals(50, coupon.getMinTransaction());
    }

    @Test
    void buildTransactionCouponWithNegativeMinTransactionShouldThrowException() {
        TransactionCouponBuilder builder = new TransactionCouponBuilder()
                .setPercentDiscount(10)
                .setFixedDiscount(5)
                .setMaxDiscount(20)
                .setSupermarketName("Supermarket")
                .setMinTransaction(-1);

        assertThrows(IllegalArgumentException.class, builder::build);
    }
}
