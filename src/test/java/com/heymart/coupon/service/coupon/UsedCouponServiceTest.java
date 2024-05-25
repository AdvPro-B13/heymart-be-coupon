package com.heymart.coupon.service.coupon;

import com.heymart.coupon.dto.CouponRequest;
import com.heymart.coupon.repository.UsedCouponRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@EnableAsync
class UsedCouponServiceTest {

    @Mock
    private UsedCouponRepository usedCouponRepository;

    @InjectMocks
    private UsedCouponServiceImpl usedCouponService;

    @BeforeEach
    void setUp() {
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
}
