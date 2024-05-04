package com.heymart.coupon.repository;

import com.heymart.coupon.model.TransactionCoupon;
import com.heymart.coupon.model.builder.TransactionCouponBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class TransactionCouponRepositoryTest {

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
    public void testFindTransactionCouponById() {
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
    public void testDeleteTransactionCoupon() {
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
    public void testFindNonExistentTransactionCoupon() {
        Optional<TransactionCoupon> found = repository.findById("nonexistentId");
        assertTrue(found.isEmpty(), "No coupon should be found with a non-existent ID");
    }

    @Test
    public void testDeleteNonExistentCoupon() {
        assertDoesNotThrow(() -> {
            repository.deleteById("nonexistentId");
        });
    }
}
