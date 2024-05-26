package com.heymart.coupon.service.coupon;

import com.heymart.coupon.dto.CouponRequest;
import com.heymart.coupon.dto.UserResponse;
import com.heymart.coupon.exception.CouponAlreadyUsedException;
import com.heymart.coupon.model.TransactionCoupon;
import com.heymart.coupon.model.UsedCoupon;
import com.heymart.coupon.model.builder.TransactionCouponBuilder;
import com.heymart.coupon.repository.UsedCouponRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@EnableAsync
class UsedCouponServiceTest {

    @Mock
    private UsedCouponRepository usedCouponRepository;

    @InjectMocks
    private UsedCouponServiceImpl usedCouponService;

    private TransactionCoupon coupon;
    private UUID couponId;
    @BeforeEach
    void setUp() {
        coupon = new TransactionCouponBuilder()
                .setPercentDiscount(10)
                .setFixedDiscount(5)
                .setMaxDiscount(20)
                .setSupermarketId("Supermarket")
                .setMinTransaction(50)
                .build();
        couponId = UUID.fromString("c0a8012b-68e6-42d9-b99e-4f318a25e3a0");
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testDeleteUsedCouponsByCouponIdWhenCouponsExist() {
        CouponRequest request = new CouponRequest();
        request.setId("c0a8012b-68e6-42d9-b99e-4f318a25e3a0");
        UUID couponId = UUID.fromString(request.getId());

        CompletableFuture<Void> future = usedCouponService.deleteUsedCouponsByCouponId(request);

        assertTrue(future.isDone());
        verify(usedCouponRepository, times(1)).deleteByCouponId(couponId);
    }

    @Test
    void testDeleteUsedCouponsByCouponIdWhenCouponsDoNotExist() {
        CouponRequest request = new CouponRequest();
        request.setId("c0a8012b-68e6-42d9-b99e-4f318a25e3a0");
        UUID couponId = UUID.fromString(request.getId());


        CompletableFuture<Void> future = usedCouponService.deleteUsedCouponsByCouponId(request);

        assertTrue(future.isDone());
        verify(usedCouponRepository, times(1)).deleteByCouponId(couponId);
        verify(usedCouponRepository, never()).deleteAll(any());
    }


    @Test
    void testUseCoupon_couponAlreadyUsed() {
        Long userId = 1L;
        when(usedCouponRepository.existsByUserIdAndCouponId(eq(1L), any())).thenReturn(true);
        assertThrows(CouponAlreadyUsedException.class, () -> {
            usedCouponService.useCoupon(coupon, userId);
        });
    }

    @Test
    void testUseCoupon_success() {
        when(usedCouponRepository.existsByUserIdAndCouponId(eq(1L), any())).thenReturn(false);
        when(usedCouponRepository.save(any(UsedCoupon.class))).thenAnswer(invocation -> invocation.getArgument(0));

        UsedCoupon result = usedCouponService.useCoupon(coupon, 1L);

        assertNotNull(result);
        assertEquals(1L, result.getUserId());

        verify(usedCouponRepository, times(1)).existsByUserIdAndCouponId(any(), any());
        verify(usedCouponRepository, times(1)).save(any(UsedCoupon.class));
    }

    @Test
    public void testGetUsedCouponBySupermarket() {
        // Setup
        String supermarketId = "someSupermarket";
        Long userId = 1L;
        List<UsedCoupon> mockCoupons = Arrays.asList(
                new UsedCoupon(UUID.randomUUID(), supermarketId, userId),
                new UsedCoupon(UUID.randomUUID(), supermarketId, userId)
        );

        when(usedCouponRepository.findBySupermarketIdAndUserId(supermarketId, userId)).thenReturn(mockCoupons);

        // Execute
        List<UsedCoupon> coupons = usedCouponService.getUsedCouponBySupermarket(supermarketId, userId);

        // Verify
        assertEquals(2, coupons.size());
        verify(usedCouponRepository, times(1)).findBySupermarketIdAndUserId(supermarketId, userId);
    }
}
