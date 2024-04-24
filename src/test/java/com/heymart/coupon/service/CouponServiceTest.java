package com.heymart.coupon.service;

import com.heymart.coupon.model.Coupon;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
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
        Coupon coupon = new Coupon();
        coupon.setId("1");
        coupon.setType("Product");
        coupon.setPercentDiscount(10);

        when(couponRepository.save(any())).thenReturn(coupon);

        Coupon savedCoupon = couponService.createCoupon(coupon);

        assertEquals(coupon.getId(), savedCoupon.getId());
        assertEquals(coupon.getType(), savedCoupon.getType());
        assertEquals(coupon.getPercentDiscount(), savedCoupon.getPercentDiscount());

        verify(couponRepository, times(1)).save(any());
    }

    @Test
    public void testEditCoupon() {
        Coupon editedCoupon = new Coupon();
        editedCoupon.setId("1");
        editedCoupon.setType("Product");
        editedCoupon.setPercentDiscount(15);

        when(couponRepository.save(any())).thenReturn(editedCoupon);

        Coupon updatedCoupon = couponService.edit(editedCoupon);

        assertEquals(editedCoupon.getId(), updatedCoupon.getId());
        assertEquals(editedCoupon.getType(), updatedCoupon.getType());
        assertEquals(editedCoupon.getPercentDiscount(), updatedCoupon.getPercentDiscount());

        verify(couponRepository, times(1)).save(any());
    }

    @Test
    public void testDeleteCoupon() {
        Coupon coupon = new Coupon();
        coupon.setId("1");
        coupon.setType("Product");
        coupon.setPercentDiscount(10);

        couponService.delete(coupon);

        verify(couponRepository, times(1)).delete(any());
    }

    @Test
    public void testFindAllCoupons() {
        List<Coupon> couponList = new ArrayList<>();
        Coupon coupon = new Coupon("id1", CouponType.PRODUCT.getValue(), 20, 100, 300, "Indomaret", "P01", 0);
        assertEquals("id1", coupon.getId());
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
