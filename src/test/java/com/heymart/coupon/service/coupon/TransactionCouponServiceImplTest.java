package com.heymart.coupon.service.coupon;

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
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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
    void createCoupon_shouldCreateCouponSuccessfully() throws ExecutionException, InterruptedException {
        CouponRequest request = new CouponRequest(null, 10, 5, 15, "Supermarket", null, 50);
        TransactionCoupon coupon = new TransactionCouponBuilder()
                .setPercentDiscount(10)
                .setFixedDiscount(5)
                .setMaxDiscount(15)
                .setSupermarketName("Supermarket")
                .setMinTransaction(50)
                .build();

        when(transactionCouponRepository.save(any(TransactionCoupon.class))).thenReturn(coupon);

        CompletableFuture<TransactionCoupon> resultFuture = transactionCouponService.createCoupon(request);
        TransactionCoupon result = resultFuture.get();

        assertNotNull(result);
        assertEquals(coupon, result);
        verify(transactionCouponRepository).save(any(TransactionCoupon.class));
    }

    @Test
    void updateCoupon_shouldUpdateCouponSuccessfully() throws ExecutionException, InterruptedException {
        CouponRequest request = new CouponRequest(null, 10, 5, 15, "Supermarket", null, 50);
        TransactionCoupon coupon = new TransactionCouponBuilder()
                .setPercentDiscount(10)
                .setFixedDiscount(5)
                .setMaxDiscount(15)
                .setSupermarketName("Supermarket")
                .setMinTransaction(50)
                .build();

        when(transactionCouponRepository.save(any(TransactionCoupon.class))).thenReturn(coupon);

        CompletableFuture<TransactionCoupon> resultFuture = transactionCouponService.createCoupon(request);
        TransactionCoupon result = resultFuture.get();

        coupon.setPercentDiscount(20);

        when(transactionCouponRepository.findById(coupon.getId())).thenReturn(Optional.of(coupon));

        resultFuture = transactionCouponService.updateCoupon(request);
        result = resultFuture.get();

        assertNotNull(result);
        verify(transactionCouponRepository).save(coupon);
    }

    @Test
    public void testUpdateNonExistingCoupon() throws ExecutionException, InterruptedException {
        CouponRequest request = new CouponRequest("non-existing-id", 20, 10, 25, "Supermarket", "123", 0);

        when(transactionCouponRepository.findById(request.getId())).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            transactionCouponService.updateCoupon(request);
        });
        assertEquals("Coupon not found", exception.getMessage());

        verify(transactionCouponRepository, times(1)).findById(request.getId());
        verify(transactionCouponRepository, never()).save(any(TransactionCoupon.class));
    }

    @Test
    void deleteCoupon_shouldDeleteCoupon() throws ExecutionException, InterruptedException {
        TransactionCoupon coupon = new TransactionCouponBuilder()
                .setPercentDiscount(10)
                .setFixedDiscount(5)
                .setMaxDiscount(15)
                .setSupermarketName("Supermarket")
                .setMinTransaction(50)
                .build();

        when(transactionCouponRepository.findById(coupon.getId())).thenReturn(Optional.of(coupon));

        CompletableFuture<Void> resultFuture = transactionCouponService.deleteCoupon(new CouponRequest(coupon.getId(), 0, 0, 0, null, null, 0));
        resultFuture.get();

        verify(transactionCouponRepository).delete(coupon);
    }

    @Test
    void deleteCoupon_shouldThrowRuntimeExceptionWhenCouponNotFound() throws ExecutionException, InterruptedException {
        CouponRequest request = new CouponRequest("nonexistent-id", 10, 5, 15, "Supermarket", "123", 0);

        when(transactionCouponRepository.findById(anyString())).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            transactionCouponService.deleteCoupon(request);
        });
        assertEquals("Coupon not found", exception.getMessage());

        verify(transactionCouponRepository).findById("nonexistent-id");
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
        List<TransactionCoupon> expectedCoupons = Arrays.asList(coupon, coupon2);
        when(transactionCouponRepository.findAll()).thenReturn(expectedCoupons);

        List<TransactionCoupon> result = transactionCouponService.findAllCoupons();
        assertNotNull(result);
        verify(transactionCouponRepository).findAll();
    }

    @Test
    public void testFindById_CouponExists() throws ExecutionException, InterruptedException {
        String couponId = "123";
        TransactionCoupon mockCoupon = new TransactionCouponBuilder()
                .setPercentDiscount(10)
                .setFixedDiscount(5)
                .setMaxDiscount(15)
                .setSupermarketName("Supermarket")
                .setMinTransaction(50)
                .build();
        when(transactionCouponRepository.findById(couponId)).thenReturn(Optional.of(mockCoupon));

        TransactionCoupon result = transactionCouponService.findById(couponId);

        assertNotNull(result);
        assertEquals(mockCoupon, result);
    }

    @Test
    public void testFindById_CouponDoesNotExist() throws ExecutionException, InterruptedException {
        String couponId = "unknown";
        when(transactionCouponRepository.findById(couponId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            transactionCouponService.findById(couponId);
        });
        assertEquals("Coupon not found", exception.getMessage());
    }

    @Test
    public void testFindBySupermarketName() {
        String supermarketName = "TestMart";
        TransactionCoupon coupon = new TransactionCouponBuilder()
                .setPercentDiscount(10)
                .setFixedDiscount(5)
                .setMaxDiscount(15)
                .setSupermarketName(supermarketName)
                .setMinTransaction(5)
                .build();

        TransactionCoupon coupon2 = new TransactionCouponBuilder()
                .setPercentDiscount(10)
                .setFixedDiscount(5)
                .setMaxDiscount(15)
                .setSupermarketName("Supermarket 2")
                .setMinTransaction(5)
                .build();
        List<TransactionCoupon> expectedCoupons = Arrays.asList(coupon);

        when(transactionCouponRepository.findBySupermarketName(supermarketName)).thenReturn(expectedCoupons);

        List<TransactionCoupon> actualCoupons = transactionCouponService.findBySupermarketName(supermarketName);

        assertEquals(expectedCoupons.size(), actualCoupons.size());
        assertEquals(expectedCoupons, actualCoupons);
    }
}
