package com.heymart.coupon.service.coupon;

import com.heymart.coupon.dto.CouponRequest;
import com.heymart.coupon.model.ProductCoupon;
import com.heymart.coupon.model.builder.ProductCouponBuilder;
import com.heymart.coupon.repository.ProductCouponRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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

        CompletableFuture<ProductCoupon> resultFuture = productCouponService.createCoupon(request);
        ProductCoupon result = resultFuture.join();

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

        CompletableFuture<ProductCoupon> resultFuture = productCouponService.updateCoupon(request);
        ProductCoupon result = resultFuture.join();

        assertNotNull(result);
        verify(productCouponRepository).save(coupon);
    }

    @Test
    void testUpdateNonExistingProductCoupon() {
        CouponRequest request = new CouponRequest("non-existing-id", 20, 10, 25, "Supermarket", "123", 0);

        when(productCouponRepository.findById(request.getId())).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            productCouponService.updateCoupon(request).join();
        });

        assertEquals("Coupon not found", exception.getMessage());

        verify(productCouponRepository, times(1)).findById(request.getId());
        verify(productCouponRepository, never()).save(any(ProductCoupon.class));
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
        CompletableFuture<Void> resultFuture = productCouponService.deleteCoupon(new CouponRequest(coupon.getId(),0,0,0,null,null,0));
        resultFuture.join();

        verify(productCouponRepository).delete(coupon);
    }

    @Test
    void deleteCoupon_shouldThrowRuntimeExceptionWhenCouponNotFound() {
        CouponRequest request = new CouponRequest("nonexistent-id", 10, 5, 15, "Supermarket", "123", 0);

        when(productCouponRepository.findById(anyString())).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            productCouponService.deleteCoupon(request).join();
        });

        assertEquals("Coupon not found", exception.getMessage());
        verify(productCouponRepository).findById("nonexistent-id");
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
                .setSupermarketName("Supermarket 2")
                .setIdProduct("123")
                .build();
        List<ProductCoupon> expectedCoupons = Arrays.asList(coupon, coupon2);
        when(productCouponRepository.findAll()).thenReturn(expectedCoupons);

        List<ProductCoupon> result = productCouponService.findAllCoupons();
        assertNotNull(result);
        assertEquals(expectedCoupons, result);
        verify(productCouponRepository).findAll();
    }

    @Test
    void testFindById_CouponExists() {
        String couponId = "123";
        ProductCoupon mockCoupon = new ProductCouponBuilder()
                .setPercentDiscount(10)
                .setFixedDiscount(5)
                .setMaxDiscount(15)
                .setSupermarketName("Supermarket")
                .setIdProduct("123")
                .build();
        when(productCouponRepository.findById(couponId)).thenReturn(Optional.of(mockCoupon));

        ProductCoupon result = productCouponService.findById(couponId);

        assertNotNull(result);
        assertEquals(mockCoupon, result);
    }

    @Test
    void testFindById_CouponDoesNotExist() {
        String couponId = "unknown";
        when(productCouponRepository.findById(couponId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            productCouponService.findById(couponId);
        });

        assertEquals("Coupon not found", exception.getMessage());
    }

    @Test
    void testFindBySupermarketName() {
        String supermarketName = "TestMart";
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

        when(productCouponRepository.findBySupermarketName(supermarketName)).thenReturn(expectedCoupons);

        List<ProductCoupon> actualCoupons = productCouponService.findBySupermarketName(supermarketName);

        assertEquals(expectedCoupons.size(), actualCoupons.size());
        assertEquals(expectedCoupons, actualCoupons);
    }

    @Test
    void testFindByIdProduct_CouponExists() {
        String idProduct = "123";
        ProductCoupon mockCoupon = new ProductCouponBuilder()
                .setPercentDiscount(10)
                .setFixedDiscount(5)
                .setMaxDiscount(15)
                .setSupermarketName("Supermarket")
                .setIdProduct("123")
                .build();
        when(productCouponRepository.findByIdProduct(idProduct)).thenReturn(mockCoupon);

        ProductCoupon result = productCouponService.findByIdProduct(idProduct);

        assertNotNull(result);
        assertEquals(mockCoupon, result);
    }

    @Test
    void testFindByIdProduct_NotFound() {
        String idProduct = "123";
        when(productCouponRepository.findByIdProduct(idProduct)).thenReturn(null);

        Exception exception = assertThrows(RuntimeException.class, () -> {
            productCouponService.findByIdProduct(idProduct);
        });

        assertEquals("Coupon not found", exception.getMessage());
    }
}
