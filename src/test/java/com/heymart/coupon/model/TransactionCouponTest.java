package com.heymart.coupon.model;

import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.*;

import com.heymart.coupon.model.*;

public class TransactionCouponTest {
    @BeforeEach
    void setUp() {
    }
    void testCreateCouponNegativeAmount() {
        assertThrows(IllegalArgumentException.class, () -> {
            new TransactionCoupon("id",-10,10,0,"Indomaret",0);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            new TransactionCoupon("id",10,-10,0,"Indomaret",0);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            new TransactionCoupon("id",10,10,-10,"Indomaret",0);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            new TransactionCoupon("id",10,10,0,"Indomaret",-10);
        });
    }
    void testCreateCouponWithMaxLessThanFixed() {
        assertThrows(IllegalArgumentException.class, () -> {
            new TransactionCoupon("id",10,10,5,"Indomaret",0);
        });
    }
    void testCreateCouponSuccess() {
        TransactionCoupon tCoupon = new TransactionCoupon("id",10,10,20,"Indomaret",0);
        assertNotNull(tCoupon.id);
        assertEquals(tCoupon.percentDiscount, 10);
        assertEquals(tCoupon.fixedDiscount, 15);
        assertEquals(tCoupon.maxDiscount, 20);
        assertEquals(tCoupon.supermarket, "Indomaret");
        assertEquals(tCoupon.minTransaction, 0);
    }
}