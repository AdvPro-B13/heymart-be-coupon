package com.heymart.coupon.service;

import com.heymart.coupon.dto.CouponRequest;
import com.heymart.coupon.model.ProductCoupon;
import com.heymart.coupon.model.builder.ProductCouponBuilder;
import com.heymart.coupon.repository.ProductCouponRepository;
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
class ProductCouponServiceImplTest {

    @Mock
    private ProductCouponRepository productCouponRepository;

    @InjectMocks
    private ProductCouponServiceImpl productCouponService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void createCoupon_shouldCreateCouponSuccessfully() {
        CouponRequest request = new CouponRequest(null,10, 5, 15, "Supermarket", "123",0);
        ProductCoupon coupon = new ProductCouponBuilder()
                .setPercentDiscount(10)
                .setFixedDiscount(5)
                .setMaxDiscount(15)
                .setSupermarketName("Supermarket")
                .setIdProduct("123")
                .build();

        when(productCouponRepository.save(any(ProductCoupon.class))).thenReturn(coupon);

        ProductCoupon result = productCouponService.createCoupon(request);
        assertNotNull(result);
        assertEquals(coupon, result);
        verify(productCouponRepository).save(any(ProductCoupon.class));
    }

    @Test
    void updateCoupon_shouldUpdateCouponSuccessfully() {
        CouponRequest request = new CouponRequest(null,10, 5, 15, "Supermarket", "123",0);
        ProductCoupon coupon = new ProductCouponBuilder()
                .setPercentDiscount(10)
                .setFixedDiscount(5)
                .setMaxDiscount(15)
                .setSupermarketName("Supermarket")
                .setIdProduct("123")
                .build();

        when(productCouponRepository.save(any(ProductCoupon.class))).thenReturn(coupon);

        productCouponService.createCoupon(request);

        coupon.setPercentDiscount(20);

        when(productCouponRepository.findById(coupon.getId())).thenReturn(Optional.of(coupon));

        ProductCoupon result = productCouponService.updateCoupon(request);
        assertNotNull(result);
        verify(productCouponRepository).save(coupon);
    }

    @Test
    void deleteCoupon_shouldDeleteCoupon() {
        ProductCoupon coupon = new ProductCouponBuilder()
                .setPercentDiscount(10)
                .setFixedDiscount(5)
                .setMaxDiscount(15)
                .setSupermarketName("Supermarket")
                .setIdProduct("123")
                .build();

        when(productCouponRepository.findById(coupon.getId())).thenReturn(Optional.of(coupon));
        productCouponService.deleteCoupon(new CouponRequest(coupon.getId(),0,0,0,null,null,0));
        verify(productCouponRepository).delete(coupon);
    }

    @Test
    void findAllCoupons_shouldReturnListOfCoupons() {
        ProductCoupon coupon = new ProductCouponBuilder()
                .setPercentDiscount(10)
                .setFixedDiscount(5)
                .setMaxDiscount(15)
                .setSupermarketName("Supermarket")
                .setIdProduct("123")
                .build();

        ProductCoupon coupon2 = new ProductCouponBuilder()
                .setPercentDiscount(10)
                .setFixedDiscount(5)
                .setMaxDiscount(15)
                .setSupermarketName("Supermarket")
                .setIdProduct("123")
                .build();
        List<ProductCoupon> expectedCoupons = Arrays.asList(coupon,coupon2);
        when(productCouponRepository.findAll()).thenReturn(expectedCoupons);

        List<ProductCoupon> result = productCouponService.findAllCoupons();
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(2, result.size());
        verify(productCouponRepository).findAll();
    }
}