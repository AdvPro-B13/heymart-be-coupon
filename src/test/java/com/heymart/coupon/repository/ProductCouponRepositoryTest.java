package com.heymart.coupon.repository;

import com.heymart.coupon.model.ProductCoupon;
import com.heymart.coupon.model.builder.ProductCouponBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ProductCouponRepositoryTest {

    @Autowired
    private ProductCouponRepository repository;
    private final String supermarketName = "supermarketName";
    private final UUID randomId = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
    @Test
    void testSaveProductCoupon() {
        ProductCoupon coupon = new ProductCouponBuilder()
                .setPercentDiscount(10)
                .setFixedDiscount(5)
                .setMaxDiscount(15)
                .setSupermarketName(supermarketName)
                .setIdProduct("123")
                .build();
        ProductCoupon saved = repository.save(coupon);
        assertNotNull(saved);
        assertNotNull(saved.getId());
    }

    @Test
    void testFindProductCouponById() {
        ProductCoupon coupon = new ProductCouponBuilder()
                .setPercentDiscount(10)
                .setFixedDiscount(5)
                .setMaxDiscount(15)
                .setSupermarketName("TestSupermarket")
                .setIdProduct("123")
                .build();
        ProductCoupon saved = repository.save(coupon);
        ProductCoupon found = repository.findById(saved.getId()).orElse(null);
        assertNotNull(found);
        assertEquals("TestSupermarket", found.getSupermarketName());
        assertEquals("123", found.getIdProduct());
    }

    @Test
    void testDeleteProductCoupon() {
        ProductCoupon coupon = new ProductCouponBuilder()
                .setPercentDiscount(10)
                .setFixedDiscount(5)
                .setMaxDiscount(15)
                .setSupermarketName(supermarketName)
                .setIdProduct("123")
                .build();ProductCoupon saved = repository.save(coupon);
        repository.delete(saved);
        assertFalse(repository.findById(saved.getId()).isPresent());
    }

    @Test
    void testFindNonExistentProductCoupon() {
        Optional<ProductCoupon> found = repository.findById(randomId);
        assertTrue(found.isEmpty(), "No coupon should be found with a non-existent ID");
    }

    @Test
    void testDeleteNonExistentCoupon() {
        assertDoesNotThrow(() -> {
            repository.deleteById(randomId);
        });
    }

    @Test
    void TestFindBySupermarketName() {
        ProductCoupon coupon = new ProductCouponBuilder()
                .setPercentDiscount(10)
                .setFixedDiscount(5)
                .setMaxDiscount(15)
                .setSupermarketName(supermarketName)
                .setIdProduct("123")
                .build();
        ProductCoupon coupon2 = new ProductCouponBuilder()
                .setPercentDiscount(10)
                .setFixedDiscount(5)
                .setMaxDiscount(15)
                .setSupermarketName(supermarketName)
                .setIdProduct("124")
                .build();
        ProductCoupon coupon3 = new ProductCouponBuilder()
                .setPercentDiscount(10)
                .setFixedDiscount(5)
                .setMaxDiscount(15)
                .setSupermarketName("Other Supermarket")
                .setIdProduct("125")
                .build();
        repository.save(coupon);
        repository.save(coupon2);
        repository.save(coupon3);
        List<ProductCoupon> expectedCoupons = Arrays.asList(coupon, coupon2);

        List<ProductCoupon> actualCoupons = repository.findBySupermarketName(supermarketName);

        assertEquals(expectedCoupons.size(), actualCoupons.size());
        assertEquals(expectedCoupons, actualCoupons);
    }
    @Test
    void testFindByProductId() {
        ProductCoupon coupon = new ProductCouponBuilder()
                .setPercentDiscount(10)
                .setFixedDiscount(5)
                .setMaxDiscount(15)
                .setSupermarketName(supermarketName)
                .setIdProduct("123")
                .build();ProductCoupon saved = repository.save(coupon);
        ProductCoupon found = repository.findByIdProduct("123");
        assertNotNull(found);
        assertEquals(supermarketName, found.getSupermarketName());
        assertEquals("123", found.getIdProduct());
    }
    @Test
    void testFindByProductId_NotFound() {
        ProductCoupon found = repository.findByIdProduct(randomId.toString());
        assertNull(found);
    }
}
