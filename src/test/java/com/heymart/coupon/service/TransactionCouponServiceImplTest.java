package com.heymart.coupon.service;

import com.heymart.coupon.dto.CouponRequest;
import com.heymart.coupon.model.TransactionCoupon;
import com.heymart.coupon.model.builder.TransactionCouponBuilder;
import com.heymart.coupon.repository.TransactionCouponRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class TransactionCouponServiceImplTest {

    @Mock
    private TransactionCouponRepository transactionCouponRepository;

    @InjectMocks
    private TransactionCouponServiceImpl transactionCouponService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void createCoupon_shouldCreateCouponSuccessfully() {
        CouponRequest request = new CouponRequest(null,10, 5, 15, "Supermarket", null,50);
        TransactionCoupon coupon = new TransactionCouponBuilder()
                .setPercentDiscount(10)
                .setFixedDiscount(5)
                .setMaxDiscount(15)
                .setSupermarketName("Supermarket")
                .setMinTransaction(50)
                .build();

        when(transactionCouponRepository.save(any(TransactionCoupon.class))).thenReturn(coupon);

        TransactionCoupon result = transactionCouponService.createCoupon(request);
        assertNotNull(result);
        assertEquals(coupon, result);
        verify(transactionCouponRepository).save(any(TransactionCoupon.class));
    }

    @Test
    void updateCoupon_shouldUpdateCouponSuccessfully() {
        CouponRequest request = new CouponRequest(null,10, 5, 15, "Supermarket", null,50);
        TransactionCoupon coupon = new TransactionCouponBuilder()
                .setPercentDiscount(10)
                .setFixedDiscount(5)
                .setMaxDiscount(15)
                .setSupermarketName("Supermarket")
                .setMinTransaction(50)
                .build();

        when(transactionCouponRepository.save(any(TransactionCoupon.class))).thenReturn(coupon);

        TransactionCoupon result = transactionCouponService.createCoupon(request);

        coupon.setPercentDiscount(20);

        when(transactionCouponRepository.findById(coupon.getId())).thenReturn(Optional.of(coupon));

        result = transactionCouponService.updateCoupon(request);
        assertNotNull(result);
        verify(transactionCouponRepository).save(coupon);
    }

    @Test
    void deleteCoupon_shouldDeleteCoupon() {
        TransactionCoupon coupon = new TransactionCouponBuilder()
                .setPercentDiscount(10)
                .setFixedDiscount(5)
                .setMaxDiscount(15)
                .setSupermarketName("Supermarket")
                .setMinTransaction(50)
                .build();

        when(transactionCouponRepository.findById(coupon.getId())).thenReturn(Optional.of(coupon));
        transactionCouponService.deleteCoupon(new CouponRequest(coupon.getId(),0,0,0,null,null,0));
        verify(transactionCouponRepository).delete(coupon);
    }

    @Test
    void findAllCoupons_shouldReturnListOfCoupons() {
        TransactionCoupon coupon = new TransactionCouponBuilder()
                .setPercentDiscount(10)
                .setFixedDiscount(5)
                .setMaxDiscount(15)
                .setSupermarketName("Supermarket")
                .setMinTransaction(50)
                .build();

        TransactionCoupon coupon2 = new TransactionCouponBuilder()
                .setPercentDiscount(10)
                .setFixedDiscount(5)
                .setMaxDiscount(15)
                .setSupermarketName("Supermarket")
                .setMinTransaction(50)
                .build();
        List<TransactionCoupon> expectedCoupons = Arrays.asList(coupon,coupon2);
        when(transactionCouponRepository.findAll()).thenReturn(expectedCoupons);

        List<TransactionCoupon> result = transactionCouponService.findAllCoupons();
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(2, result.size());
        verify(transactionCouponRepository).findAll();
    }
}
