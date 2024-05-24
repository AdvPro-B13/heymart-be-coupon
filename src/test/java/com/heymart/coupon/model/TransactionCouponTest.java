package com.heymart.coupon.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TransactionCouponTest {

    @Test
    void validTransactionCoupon() {
        TransactionCoupon coupon = new TransactionCoupon(10, 5, 20, "Supermarket", 50);
        assertNotNull(coupon);
        assertEquals(10, coupon.getPercentDiscount());
        assertEquals(5, coupon.getFixedDiscount());
        assertEquals(20, coupon.getMaxDiscount());
        assertEquals("Supermarket", coupon.getSupermarketId());
        assertEquals(50, coupon.getMinTransaction());
    }

    @Test
    void negativePercentDiscountShouldThrow() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                new TransactionCoupon(-1, 5, 15, "Supermarket", 50)
        );
        assertTrue(ex.getMessage().contains("Percent discount cannot be negative"));
    }

    @Test
    void negativeFixedDiscountShouldThrow() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                new TransactionCoupon(10, -5, 15, "Supermarket", 50)
        );
        assertTrue(ex.getMessage().contains("Fixed discount cannot be negative"));
    }

    @Test
    void maxDiscountLessThanFixedDiscountShouldThrow() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                new TransactionCoupon(10, 20, 15, "Supermarket", 50)
        );
        assertTrue(ex.getMessage().contains("Max discount must be greater than or equal to fixed discount"));
    }

    @Test
    void nullSupermarketIdShouldThrow() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                new TransactionCoupon(10, 5, 15, null, 50)
        );
        assertTrue(ex.getMessage().contains("Supermarket Id cannot be empty"));
    }

    @Test
    void negativeMinTransactionShouldThrow() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                new TransactionCoupon(10, 5, 20, "Supermarket", -1)
        );
        assertTrue(ex.getMessage().contains("Minimum transaction cannot be negative"));
    }
}
