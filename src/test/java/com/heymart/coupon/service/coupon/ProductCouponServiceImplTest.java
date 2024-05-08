package com.heymart.coupon.service.coupon;

import com.heymart.coupon.dto.CouponRequest;
import com.heymart.coupon.model.ProductCoupon;
import com.heymart.coupon.model.builder.ProductCouponBuilder;
import com.heymart.coupon.repository.CouponRepository;
import com.heymart.coupon.service.coupon.ProductCouponServiceImpl;
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
    private CouponRepository<ProductCoupon> productCouponRepository;

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
    public void testUpdateNonExistingProductCoupon() {
        CouponRequest request = new CouponRequest("non-existing-id", 20, 10, 25, "Supermarket", "123", 0);

        when(productCouponRepository.findById(request.getId())).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            productCouponService.updateCoupon(request);
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
        productCouponService.deleteCoupon(new CouponRequest(coupon.getId(),0,0,0,null,null,0));
        verify(productCouponRepository).delete(coupon);
    }

    @Test
    void deleteCoupon_shouldThrowRuntimeExceptionWhenCouponNotFound() {
        CouponRequest request = new CouponRequest("nonexistent-id", 10, 5, 15, "Supermarket", "123", 0);

        when(productCouponRepository.findById(anyString())).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            productCouponService.deleteCoupon(request);
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
    @Test
    public void testFindById_CouponExists() {
        // Setup
        String couponId = "123";
        ProductCoupon mockCoupon = new ProductCouponBuilder()
                .setPercentDiscount(10)
                .setFixedDiscount(5)
                .setMaxDiscount(15)
                .setSupermarketName("Supermarket")
                .setIdProduct("123")
                .build();
        when(productCouponRepository.findById(couponId)).thenReturn(Optional.of(mockCoupon));

        // Execute
        ProductCoupon result = productCouponService.findById(couponId);

        // Verify
        assertNotNull(result);
        assertEquals(mockCoupon, result);
    }

    @Test
    public void testFindById_CouponDoesNotExist() {
        // Setup
        String couponId = "unknown";
        when(productCouponRepository.findById(couponId)).thenReturn(Optional.empty());

        // Execute
        Exception exception = assertThrows(RuntimeException.class, () -> {
            productCouponService.findById(couponId);
        });
    }
}
