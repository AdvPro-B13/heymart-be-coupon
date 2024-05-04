package com.heymart.coupon.repository;

import com.heymart.coupon.model.ProductCoupon;
import com.heymart.coupon.model.builder.ProductCouponBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class ProductCouponRepositoryTest {

    @Autowired
    private ProductCouponRepository repository;

    @Test
    public void testSaveProductCoupon() {
        ProductCoupon coupon = new ProductCouponBuilder()
                .setPercentDiscount(10)
                .setFixedDiscount(5)
                .setMaxDiscount(15)
                .setSupermarketName("Supermarket")
                .setIdProduct("123")
                .build();
        ProductCoupon saved = repository.save(coupon);
        assertNotNull(saved);
        assertNotNull(saved.getId());
    }

    @Test
    public void testFindProductCouponById() {
        ProductCoupon coupon = new ProductCouponBuilder()
                .setPercentDiscount(10)
                .setFixedDiscount(5)
                .setMaxDiscount(15)
                .setSupermarketName("TestSupermarket")
                .setIdProduct("123")
                .build();ProductCoupon saved = repository.save(coupon);
        ProductCoupon found = repository.findById(saved.getId()).orElse(null);
        assertNotNull(found);
        assertEquals("TestSupermarket", found.getSupermarketName());
        assertEquals("123", found.getIdProduct());
    }

    @Test
    public void testDeleteProductCoupon() {
        ProductCoupon coupon = new ProductCouponBuilder()
                .setPercentDiscount(10)
                .setFixedDiscount(5)
                .setMaxDiscount(15)
                .setSupermarketName("Supermarket")
                .setIdProduct("123")
                .build();ProductCoupon saved = repository.save(coupon);
        repository.delete(saved);
        assertFalse(repository.findById(saved.getId()).isPresent());
    }

    @Test
    public void testFindNonExistentProductCoupon() {
        Optional<ProductCoupon> found = repository.findById("nonexistentId");
        assertTrue(found.isEmpty(), "No coupon should be found with a non-existent ID");
    }

    @Test
    public void testDeleteNonExistentCoupon() {
        assertDoesNotThrow(() -> {
            repository.deleteById("nonexistentId");
        });
    }
}
