package com.heymart.coupon.repository;

import com.heymart.coupon.model.TransactionCoupon;
import com.heymart.coupon.model.builder.TransactionCouponBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class TransactionCouponRepositoryTest {

    @Autowired
    private TransactionCouponRepository repository;

    @Test
    public void testSaveTransactionCoupon() {
        TransactionCoupon coupon = new TransactionCouponBuilder()
                .setPercentDiscount(10)
                .setFixedDiscount(5)
                .setMaxDiscount(20)
                .setSupermarketName("Supermarket")
                .setMinTransaction(50)
                .build();
        TransactionCoupon saved = repository.save(coupon);
        assertNotNull(saved);
        assertNotNull(saved.getId());
    }

    @Test
    void testFindTransactionCouponById() {
        TransactionCoupon coupon = new TransactionCouponBuilder()
                .setPercentDiscount(10)
                .setFixedDiscount(5)
                .setMaxDiscount(20)
                .setSupermarketName("Supermarket")
                .setMinTransaction(50)
                .build();TransactionCoupon saved = repository.save(coupon);
        TransactionCoupon found = repository.findById(saved.getId()).orElse(null);
        assertNotNull(found);
        assertEquals(50, found.getMinTransaction());
    }

    @Test
    void testDeleteTransactionCoupon() {
        TransactionCoupon coupon = new TransactionCouponBuilder()
                .setPercentDiscount(10)
                .setFixedDiscount(5)
                .setMaxDiscount(20)
                .setSupermarketName("Supermarket")
                .setMinTransaction(50)
                .build();TransactionCoupon saved = repository.save(coupon);
        repository.delete(saved);
        assertFalse(repository.findById(saved.getId()).isPresent());
    }

    @Test
    void testFindNonExistentTransactionCoupon() {
        Optional<TransactionCoupon> found = repository.findById("nonexistentId");
        assertTrue(found.isEmpty(), "No coupon should be found with a non-existent ID");
    }

    @Test
    void testDeleteNonExistentCoupon() {
        assertDoesNotThrow(() -> {
            repository.deleteById("nonexistentId");
        });
    }
    @Test
    void TestFindBySupermarketName() {
        TransactionCoupon coupon = new TransactionCouponBuilder()
                .setPercentDiscount(10)
                .setFixedDiscount(5)
                .setMaxDiscount(15)
                .setSupermarketName("Supermarket")
                .setMinTransaction(0)
                .build();
        TransactionCoupon coupon2 = new TransactionCouponBuilder()
                .setPercentDiscount(10)
                .setFixedDiscount(5)
                .setMaxDiscount(15)
                .setSupermarketName("Supermarket")
                .setMinTransaction(0)
                .build();
        TransactionCoupon coupon3 = new TransactionCouponBuilder()
                .setPercentDiscount(10)
                .setFixedDiscount(5)
                .setMaxDiscount(15)
                .setSupermarketName("Other Supermarket")
                .setMinTransaction(0)
                .build();
        repository.save(coupon);
        repository.save(coupon2);
        repository.save(coupon3);
        List<TransactionCoupon> expectedCoupons = Arrays.asList(coupon, coupon2);

        List<TransactionCoupon> actualCoupons = repository.findBySupermarketName("Supermarket");

        assertEquals(expectedCoupons.size(), actualCoupons.size());
        assertEquals(expectedCoupons, actualCoupons);

    }
}
