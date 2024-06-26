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
        assertEquals("Supermarket", coupon.getSupermarketId());
        assertEquals("123", coupon.getIdProduct());
    }
    @Test
    void zeroPercentDiscountAndFixedDiscount() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                new ProductCoupon(0, 0, 15, "Supermarket", "123")
        );
        assertEquals("Percent discount and Fixed discount cannot both be zero",ex.getMessage());
    }
    @Test
    void negativePercentDiscountShouldThrow() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                new ProductCoupon(-1, 5, 15, "Supermarket", "123")
        );
        assertTrue(ex.getMessage().contains("Percent discount cannot be negative"));
    }

    @Test
    void moreThanHundredPercentDiscountShouldThrow() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                new ProductCoupon(101, 5, 15, "Supermarket", "123")
        );
        assertTrue(ex.getMessage().contains("Percent discount cannot be more than 100"));
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
    void nullSupermarketIdShouldThrow() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                new ProductCoupon(10, 5, 15, null, "123")
        );
        assertTrue(ex.getMessage().contains("Supermarket Id cannot be empty"));
    }
    @Test
    void emptySupermarketIdShouldThrow() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                new ProductCoupon(10, 5, 15, "", "123")
        );
        assertTrue(ex.getMessage().contains("Supermarket Id cannot be empty"));
    }
    @Test
    void negativePercentDiscount() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                new ProductCoupon(-10, 5, 15, "supermarket", "123")
        );
        assertTrue(ex.getMessage().contains("Percent discount cannot be negative"));
    }
    @Test
    void negativeFixedDiscount() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                new ProductCoupon(10, -5, 15, "supermarket", "123")
        );
        assertTrue(ex.getMessage().contains("Fixed discount cannot be negative"));
    }
    @Test
    void tooSmallMaxDiscount() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                new ProductCoupon(10, 15, 5, "supermarket", "123")
        );
        assertTrue(ex.getMessage().contains("Max discount must be greater than or equal to fixed discount"));
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
    @Test
    void setNegativePercentDiscount() {
        ProductCoupon coupon = new ProductCoupon(10, 5, 20, "Supermarket", "123");
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
            coupon.setPercentDiscount(-10)
        );
        assertTrue(ex.getMessage().contains("Percent discount cannot be negative"));
    }
    @Test
    void setGreaterThanHundrtedPercentDiscount() {
        ProductCoupon coupon = new ProductCoupon(10, 5, 20, "Supermarket", "123");
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                coupon.setPercentDiscount(110)
        );
        assertTrue(ex.getMessage().contains("Percent discount cannot be more than 100"));
    }
    @Test
    void setNegativeFixedDiscount() {
        ProductCoupon coupon = new ProductCoupon(10, 5, 20, "Supermarket", "123");
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                coupon.setFixedDiscount(-10)
        );
        assertTrue(ex.getMessage().contains("Fixed discount cannot be negative"));
    }
    @Test
    void setTooBigFixedDiscount() {
        ProductCoupon coupon = new ProductCoupon(10, 5, 20, "Supermarket", "123");
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                coupon.setFixedDiscount(30)
        );
        assertTrue(ex.getMessage().contains("Fixed discount must be smaller than or equal to Max Discount"));
    }
    @Test
    void setTooSmallMaxDiscount() {
        ProductCoupon coupon = new ProductCoupon(10, 5, 20, "Supermarket", "123");
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                coupon.setMaxDiscount(1)
        );
        assertTrue(ex.getMessage().contains("Max discount must be greater than or equal to fixed discount"));
    }
}
