package com.heymart.coupon.service;

import com.heymart.coupon.model.Coupon;
import com.heymart.coupon.model.builder.CouponBuilder;
import com.heymart.coupon.model.enums.CouponType;
import com.heymart.coupon.repository.CouponRepository;
import com.heymart.coupon.service.CouponService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CouponServiceTest {

    @InjectMocks
    private CouponServiceImpl couponService;

    @Mock
    private CouponRepository couponRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateAndFind() {
        Coupon coupon = new CouponBuilder()
                .setType(CouponType.PRODUCT.getValue())
                .setPercentDiscount(20)
                .setFixedDiscount(100)
                .setMaxDiscount(300)
                .setSupermarket("Indomaret")
                .setIdProduct("P01")
                .getResult();;

        when(couponRepository.save(any())).thenReturn(coupon);

        Coupon savedCoupon = couponService.createCoupon(coupon);

        assertEquals(coupon.getId(), savedCoupon.getId());
        assertEquals(coupon.getType(), savedCoupon.getType());
        assertEquals(coupon.getPercentDiscount(), savedCoupon.getPercentDiscount());

        verify(couponRepository, times(1)).save(any());
    }

    @Test
    public void testEditCoupon() {
        Coupon coupon = new CouponBuilder()
                .setType(CouponType.PRODUCT.getValue())
                .setPercentDiscount(20)
                .setFixedDiscount(100)
                .setMaxDiscount(300)
                .setSupermarket("Indomaret")
                .setIdProduct("P01")
                .getResult();;

        when(couponRepository.save(any())).thenReturn(coupon);
        coupon.setMaxDiscount(250);
        Coupon updatedCoupon = couponService.edit(coupon);

        assertEquals(coupon.getId(), updatedCoupon.getId());
        assertEquals(coupon.getMaxDiscount(), 250);
        assertEquals(updatedCoupon.getMaxDiscount(), 250);
        verify(couponRepository, times(1)).save(any());
    }

    @Test
    public void testDeleteCoupon() {
        Coupon coupon = new CouponBuilder()
                .setType(CouponType.PRODUCT.getValue())
                .setPercentDiscount(20)
                .setFixedDiscount(100)
                .setMaxDiscount(300)
                .setSupermarket("Indomaret")
                .setIdProduct("P01")
                .getResult();

        List<Coupon> couponList = new ArrayList<>();
        couponList.add(coupon);
        when(couponRepository.findAll()).thenReturn(couponList);
        List<Coupon> foundCoupons = couponService.findAll();

        couponService.delete(coupon);
        List<Coupon> couponList2 = new ArrayList<>();
        when(couponRepository.findAll()).thenReturn(couponList2);
        List<Coupon> foundCoupons2 = couponService.findAll();

        assertFalse(foundCoupons.isEmpty());
        assertTrue(foundCoupons2.isEmpty());
        verify(couponRepository, times(1)).delete(any());
    }

    @Test
    public void testFindAllCoupons() {
        List<Coupon> couponList = new ArrayList<>();
        Coupon coupon = new CouponBuilder()
                .setType(CouponType.PRODUCT.getValue())
                .setPercentDiscount(20)
                .setFixedDiscount(100)
                .setMaxDiscount(300)
                .setSupermarket("Indomaret")
                .setIdProduct("P01")
                .getResult();;

        assertEquals(CouponType.PRODUCT.getValue(), coupon.getType());
        assertEquals(20, coupon.getPercentDiscount());
        assertEquals(100, coupon.getFixedDiscount());
        assertEquals(300, coupon.getMaxDiscount());
        assertEquals("Indomaret", coupon.getSupermarket());
        assertEquals("P01", coupon.getIdProduct());
        assertEquals(0, coupon.getMinTransaction());
        couponList.add(coupon);

        when(couponRepository.findAll()).thenReturn(couponList);

        List<Coupon> foundCoupons = couponService.findAll();

        assertEquals(couponList.size(), foundCoupons.size());
        assertEquals(couponList.get(0).getId(), foundCoupons.get(0).getId());
        assertEquals(couponList.get(0).getType(), foundCoupons.get(0).getType());
        assertEquals(couponList.get(0).getPercentDiscount(), foundCoupons.get(0).getPercentDiscount());

        verify(couponRepository, times(1)).findAll();
    }
}
