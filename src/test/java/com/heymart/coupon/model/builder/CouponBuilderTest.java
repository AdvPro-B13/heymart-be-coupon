package com.heymart.coupon.model.builder;
import com.heymart.coupon.model.Coupon;
import com.heymart.coupon.model.enums.CouponType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

public class CouponBuilderTest {
    @BeforeEach
    void setUp() {
    }

    @Test
    void testCouponBuilder() {
        Coupon coupon = new CouponBuilder()
                .setId("id1")
                .setType(CouponType.PRODUCT.getValue())
                .setPercentDiscount(20)
                .setFixedDiscount(100)
                .setMaxDiscount(300)
                .setSupermarket("Indomaret")
                .setIdProduct("P01")
                .setMinTransaction(0)
                .getResult();

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
    void testCouponBuilderWithNullValues() {
        assertThrows(IllegalArgumentException.class, () -> {
            Coupon coupon = new CouponBuilder()
                    .setId(null)
                    .setType(null)
                    .setPercentDiscount(-20)
                    .setFixedDiscount(-100)
                    .setMaxDiscount(-300)
                    .setSupermarket(null)
                    .setIdProduct(null)
                    .setMinTransaction(-100)
                    .getResult();
        });
    }
}
