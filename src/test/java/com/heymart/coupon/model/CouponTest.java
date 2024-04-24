package com.heymart.coupon.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.heymart.coupon.model.enums.CouponType;

public class CouponTest {
    @BeforeEach
    void setUp() {
    }
    @Test
    void testCreateCouponNegativeAmount() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Coupon("id",CouponType.PRODUCT.getValue(), -10,10,0,"Indomaret","P01",0);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            new Coupon("id",CouponType.PRODUCT.getValue(),10,-10,0,"Indomaret","P01",0);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            new Coupon("id",CouponType.PRODUCT.getValue(),10,10,-10,"Indomaret", "P01",0);
        });
    }
    @Test
    void testCreateValidCoupon() {
        Coupon coupon = new Coupon("id1", CouponType.PRODUCT.getValue(), 20, 100, 300, "Indomaret", "P01", 0);
        assertEquals("id1", coupon.getId());
        assertEquals(CouponType.PRODUCT.getValue(), coupon.getType());
        assertEquals(20, coupon.getPercentDiscount());
        assertEquals(100, coupon.getFixedDiscount());
        assertEquals(300, coupon.getMaxDiscount());
        assertEquals("Indomaret", coupon.getSupermarket());
        assertEquals("P01", coupon.getIdProduct());
        assertEquals(0, coupon.getMinTransaction());
    }

    @Test
    void testCreateCouponWithInvalidType() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Coupon("id2", "INVALID_TYPE", 10, 50, 150, "Indomaret", "P01", 50);
        });
    }
    @Test
    void testCreateCouponWithNullId() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Coupon(null, CouponType.PRODUCT.getValue(), 10, 50, 150, "Indomaret", "P01", 50);
        });
    }

    @Test
    void testCreateCouponWithNullSupermarket() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Coupon("id3", CouponType.PRODUCT.getValue(), 10, 50, 150, null, "P01", 50);
        });
    }
    @Test
    void testProductCouponWithMinTransactionGreaterThanZeroAndNonNullIdProduct() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Coupon("id4", CouponType.PRODUCT.getValue(), 10, 10, 100, "Indomaret", "P01", 1);
        });
    }
    @Test
    void testCreateCouponWithInvalidDiscountRange() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Coupon("id5", CouponType.PRODUCT.getValue(), 10, 100, 100, "Indomaret", null, 0);
        });
    }
}
