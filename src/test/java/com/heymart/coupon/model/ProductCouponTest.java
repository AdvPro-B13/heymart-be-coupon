package com.heymart.coupon.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ProductCouponTest {

    @Test
    void validProductCouponShouldNotThrow() {
        ProductCoupon coupon = new ProductCoupon(10, 5, 20, "Supermarket", "123");
        assertNotNull(coupon);
        assertEquals(10, coupon.getPercentDiscount());
        assertEquals(5, coupon.getFixedDiscount());
        assertEquals(20, coupon.getMaxDiscount());
        assertEquals("Supermarket", coupon.getSupermarketName());
        assertEquals("123", coupon.getIdProduct());
    }

    @Test
    void negativePercentDiscountShouldThrow() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                new ProductCoupon(-1, 5, 15, "Supermarket", "123")
        );
        assertTrue(ex.getMessage().contains("Percent discount cannot be negative"));
    }

    @Test
    void negativeFixedDiscountShouldThrow() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                new ProductCoupon(10, -5, 15, "Supermarket", "123")
        );
        assertTrue(ex.getMessage().contains("Fixed discount cannot be negative"));
    }

    @Test
    void maxDiscountLessThanFixedDiscountShouldThrow() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                new ProductCoupon(10, 20, 15, "Supermarket", "123")
        );
        assertTrue(ex.getMessage().contains("Max discount must be greater than or equal to fixed discount"));
    }

    @Test
    void nullSupermarketNameShouldThrow() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                new ProductCoupon(10, 5, 15, null, "123")
        );
        assertTrue(ex.getMessage().contains("Supermarket name cannot be empty"));
    }

    @Test
    void nullProductIdShouldThrow() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                new ProductCoupon(10, 5, 20, "Supermarket", null)
        );
        assertTrue(ex.getMessage().contains("Product ID cannot be empty"));
    }

    @Test
    void emptyProductIdShouldThrow() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                new ProductCoupon(10, 5, 20, "Supermarket", "")
        );
        assertTrue(ex.getMessage().contains("Product ID cannot be empty"));
    }
}