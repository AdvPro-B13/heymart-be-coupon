package com.heymart.coupon.model.builder;

import com.heymart.coupon.model.ProductCoupon;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ProductCouponBuilderTest {

    @Test
    void buildValidProductCoupon() {
        ProductCouponBuilder builder = new ProductCouponBuilder()
                .setPercentDiscount(10)
                .setFixedDiscount(5)
                .setMaxDiscount(15)
                .setSupermarketName("Supermarket")
                .setIdProduct("123");

        ProductCoupon coupon = builder.build();
        assertNotNull(coupon);
        assertEquals(10, coupon.getPercentDiscount());
        assertEquals(5, coupon.getFixedDiscount());
        assertEquals(15, coupon.getMaxDiscount());
        assertEquals("Supermarket", coupon.getSupermarketName());
        assertEquals("123", coupon.getIdProduct());
    }

    @Test
    void buildProductCouponWithInvalidIdProductShouldThrowException() {
        ProductCouponBuilder builder = new ProductCouponBuilder()
                .setPercentDiscount(10)
                .setFixedDiscount(5)
                .setMaxDiscount(15)
                .setSupermarketName("Supermarket")
                .setIdProduct("");

        assertThrows(IllegalArgumentException.class, builder::build);
    }
}
