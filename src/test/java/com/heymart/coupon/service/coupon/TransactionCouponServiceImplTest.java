package com.heymart.coupon.service.coupon;

import com.heymart.coupon.dto.CouponRequest;
import com.heymart.coupon.enums.ErrorStatus;
import com.heymart.coupon.model.TransactionCoupon;
import com.heymart.coupon.model.builder.TransactionCouponBuilder;
import com.heymart.coupon.repository.CouponRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TransactionCouponServiceImplTest {

    @Mock
    private CouponRepository<TransactionCoupon> transactionCouponRepository;

    @InjectMocks
    private TransactionCouponServiceImpl transactionCouponService;
    private final UUID randomId = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createCoupon_shouldCreateCouponSuccessfully() throws ExecutionException, InterruptedException {
        CouponRequest request = new CouponRequest(null, 10, 5, 15, "Supermarket", null, 50);
        TransactionCoupon coupon = new TransactionCouponBuilder()
                .setPercentDiscount(10)
                .setFixedDiscount(5)
                .setMaxDiscount(15)
                .setSupermarketId("Supermarket")
                .setMinTransaction(50)
                .build();

        when(transactionCouponRepository.save(any(TransactionCoupon.class))).thenReturn(coupon);

        TransactionCoupon result = transactionCouponService.createCoupon(request);

        assertNotNull(result);
        assertEquals(coupon, result);
        verify(transactionCouponRepository).save(any(TransactionCoupon.class));
    }

    @Test
    void updateCoupon_shouldUpdateCouponSuccessfully() throws ExecutionException, InterruptedException {
        CouponRequest request = new CouponRequest(randomId.toString(), 10, 5, 15, "Supermarket", null, 50);
        TransactionCoupon coupon = new TransactionCouponBuilder()
                .setPercentDiscount(10)
                .setFixedDiscount(5)
                .setMaxDiscount(15)
                .setSupermarketId("Supermarket")
                .setMinTransaction(50)
                .build();

        when(transactionCouponRepository.save(any(TransactionCoupon.class))).thenReturn(coupon);

        coupon.setPercentDiscount(20);

        when(transactionCouponRepository.findById(randomId)).thenReturn(Optional.of(coupon));

        TransactionCoupon result = transactionCouponService.updateCoupon(request);

        assertNotNull(result);
        verify(transactionCouponRepository).save(coupon);
    }

    @Test
    void testUpdateNonExistingCoupon() {
        CouponRequest request = new CouponRequest(randomId.toString(), 20, 10, 25, "Supermarket", "123", 0);

        when(transactionCouponRepository.findById(UUID.fromString(randomId.toString()))).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> transactionCouponService.updateCoupon(request));
        assertEquals(ErrorStatus.COUPON_NOT_FOUND.getValue(), exception.getMessage());

        verify(transactionCouponRepository, times(1)).findById(UUID.fromString(request.getId()));
        verify(transactionCouponRepository, never()).save(any(TransactionCoupon.class));
    }

    @Test
    void deleteCoupon_shouldDeleteCoupon() throws ExecutionException, InterruptedException {
        TransactionCoupon coupon = new TransactionCouponBuilder()
                .setPercentDiscount(10)
                .setFixedDiscount(5)
                .setMaxDiscount(15)
                .setSupermarketId("Supermarket")
                .setMinTransaction(50)
                .build();

        when(transactionCouponRepository.findById(randomId)).thenReturn(Optional.of(coupon));

        CompletableFuture<Void> resultFuture = transactionCouponService.deleteCoupon(new CouponRequest(randomId.toString(), 0, 0, 0, null, null, 0));
        resultFuture.get();

        verify(transactionCouponRepository).delete(coupon);
    }

    @Test
    void deleteCoupon_shouldThrowRuntimeExceptionWhenCouponNotFound() {
        CouponRequest request = new CouponRequest(randomId.toString(), 10, 5, 15, "Supermarket", "123", 0);

        when(transactionCouponRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> transactionCouponService.deleteCoupon(request));
        assertEquals(ErrorStatus.COUPON_NOT_FOUND.getValue(), exception.getMessage());

        verify(transactionCouponRepository).findById(randomId);
    }

    @Test
    void findAllCoupons_shouldReturnListOfCoupons() {
        TransactionCoupon coupon = new TransactionCouponBuilder()
                .setPercentDiscount(10)
                .setFixedDiscount(5)
                .setMaxDiscount(15)
                .setSupermarketId("Supermarket")
                .setMinTransaction(50)
                .build();

        TransactionCoupon coupon2 = new TransactionCouponBuilder()
                .setPercentDiscount(10)
                .setFixedDiscount(5)
                .setMaxDiscount(15)
                .setSupermarketId("Supermarket")
                .setMinTransaction(50)
                .build();
        List<TransactionCoupon> expectedCoupons = Arrays.asList(coupon, coupon2);
        when(transactionCouponRepository.findAll()).thenReturn(expectedCoupons);

        List<TransactionCoupon> result = transactionCouponService.findAllCoupons();
        assertNotNull(result);
        verify(transactionCouponRepository).findAll();
    }

    @Test
    void testFindById_CouponExists() {
        TransactionCoupon mockCoupon = new TransactionCouponBuilder()
                .setPercentDiscount(10)
                .setFixedDiscount(5)
                .setMaxDiscount(15)
                .setSupermarketId("Supermarket")
                .setMinTransaction(50)
                .build();
        when(transactionCouponRepository.findById(randomId)).thenReturn(Optional.of(mockCoupon));

        TransactionCoupon result = transactionCouponService.findById(randomId.toString());

        assertNotNull(result);
        assertEquals(mockCoupon, result);
    }

    @Test
    void testFindById_CouponDoesNotExist() {
        when(transactionCouponRepository.findById(randomId)).thenReturn(Optional.empty());
        String randomIdString = randomId.toString();
        Exception exception = assertThrows(RuntimeException.class, () ->
            transactionCouponService.findById(randomIdString)
        );
        assertEquals(ErrorStatus.COUPON_NOT_FOUND.getValue(), exception.getMessage());
    }

    @Test
    void testFindBySupermarketId() {
        String supermarketId = "TestMart";
        TransactionCoupon coupon = new TransactionCouponBuilder()
                .setPercentDiscount(10)
                .setFixedDiscount(5)
                .setMaxDiscount(15)
                .setSupermarketId(supermarketId)
                .setMinTransaction(5)
                .build();

        List<TransactionCoupon> expectedCoupons = Collections.singletonList(coupon);

        when(transactionCouponRepository.findBySupermarketId(supermarketId)).thenReturn(expectedCoupons);

        List<TransactionCoupon> actualCoupons = transactionCouponService.findBySupermarketId(supermarketId);

        assertEquals(expectedCoupons.size(), actualCoupons.size());
        assertEquals(expectedCoupons, actualCoupons);
    }
}
