package com.heymart.coupon.service.coupon;

import com.heymart.coupon.dto.CouponRequest;
import com.heymart.coupon.model.UsedCoupon;
import com.heymart.coupon.repository.UsedCouponRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@EnableAsync
public class UsedCouponServiceTest {

    @Mock
    private UsedCouponRepository usedCouponRepository;

    @InjectMocks
    private UsedCouponServiceImpl usedCouponService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testDeleteUsedCouponsByCouponIdWhenCouponsExist() {
        CouponRequest request = new CouponRequest();
        request.setId("c0a8012b-68e6-42d9-b99e-4f318a25e3a0");
        UUID couponId = UUID.fromString(request.getId());
        UsedCoupon usedCoupon = new UsedCoupon();
        List<UsedCoupon> usedCoupons = Arrays.asList(usedCoupon);

        when(usedCouponRepository.findByCoupon_Id(couponId)).thenReturn(usedCoupons);

        CompletableFuture<Void> future = usedCouponService.deleteUsedCouponsByCouponId(request);

        assertTrue(future.isDone());
        verify(usedCouponRepository, times(1)).findByCoupon_Id(couponId);
        verify(usedCouponRepository, times(1)).deleteAll(usedCoupons);
    }

    @Test
    public void testDeleteUsedCouponsByCouponIdWhenCouponsDoNotExist() {
        CouponRequest request = new CouponRequest();
        request.setId("c0a8012b-68e6-42d9-b99e-4f318a25e3a0");
        UUID couponId = UUID.fromString(request.getId());

        when(usedCouponRepository.findByCoupon_Id(couponId)).thenReturn(Collections.emptyList());

        CompletableFuture<Void> future = usedCouponService.deleteUsedCouponsByCouponId(request);

        assertTrue(future.isDone());
        verify(usedCouponRepository, times(1)).findByCoupon_Id(couponId);
        verify(usedCouponRepository, never()).deleteAll(any());
    }
}
