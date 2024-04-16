package com.heymart.coupon.model;

import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.*;

import com.heymart.coupon.model.ProductCoupon;
import org.junit.jupiter.api.Test;

public class ProductCouponTest {
    @BeforeEach
    void setUp() {
    }
    @Test
    void testCreateCouponNegativeAmount() {
        assertThrows(IllegalArgumentException.class, () -> {
            new ProductCoupon("id",-10,10,0,"Indomaret","Oreo");
        });
        assertThrows(IllegalArgumentException.class, () -> {
            new ProductCoupon("id",10,-10,0,"Indomaret","Oreo");
        });
        assertThrows(IllegalArgumentException.class, () -> {
            new ProductCoupon("id",10,10,-10,"Indomaret", "Oreo");
        });
    }
    @Test
    void testCreateCouponWithMaxLessThanFixed() {
        assertThrows(IllegalArgumentException.class, () -> {
            new ProductCoupon("id",10,10,5,"Indomaret", "Oreo");
        });
    }
    @Test
    void testCreateCouponSuccess() {
        ProductCoupon pCoupon = new ProductCoupon("id",10,15,20,"Indomaret", "Oreo");
        assertNotNull(pCoupon.id);
        assertEquals(pCoupon.percentDiscount, 10);
        assertEquals(pCoupon.fixedDiscount, 15);
        assertEquals(pCoupon.maxDiscount, 20);
        assertEquals(pCoupon.supermarket, "Indomaret");
        assertEquals(pCoupon.product, "Oreo");
    }
}