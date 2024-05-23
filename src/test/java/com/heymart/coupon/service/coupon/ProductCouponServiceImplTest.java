package com.heymart.coupon.service.coupon;

import com.heymart.coupon.dto.CouponRequest;
import com.heymart.coupon.enums.ErrorStatus;
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
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductCouponServiceImplTest {

    @Mock
    private ProductCouponRepository productCouponRepository;

    @InjectMocks
    private ProductCouponServiceImpl productCouponService;
    private final UUID randomId = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
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
        CouponRequest request = new CouponRequest(randomId.toString(),10, 5, 15, "Supermarket", "123",0);
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

        when(productCouponRepository.findById(randomId)).thenReturn(Optional.of(coupon));

        ProductCoupon result = productCouponService.updateCoupon(request);

        assertNotNull(result);
        verify(productCouponRepository).save(coupon);
    }

    @Test
    void testUpdateNonExistingProductCoupon() {
        CouponRequest request = new CouponRequest(randomId.toString(), 20, 10, 25, "Supermarket", "123", 0);

        when(productCouponRepository.findById(UUID.fromString(request.getId()))).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            productCouponService.updateCoupon(request);
        });
        assertEquals(ErrorStatus.COUPON_NOT_FOUND.getValue(), exception.getMessage());

        verify(productCouponRepository, times(1)).findById(UUID.fromString(request.getId()));
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

        when(productCouponRepository.findById(randomId)).thenReturn(Optional.of(coupon));
        CompletableFuture<Void> resultFuture = productCouponService.deleteCoupon(new CouponRequest(randomId.toString(),0,0,0,null,null,0));
        resultFuture.join();

        verify(productCouponRepository).delete(coupon);
    }

    @Test
    void deleteCoupon_shouldThrowRuntimeExceptionWhenCouponNotFound() {
        CouponRequest request = new CouponRequest(randomId.toString(), 10, 5, 15, "Supermarket", "123", 0);

        when(productCouponRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            productCouponService.deleteCoupon(request);
        });

        assertEquals(ErrorStatus.COUPON_NOT_FOUND.getValue(), exception.getMessage());
        verify(productCouponRepository).findById(randomId);
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
        ProductCoupon mockCoupon = new ProductCouponBuilder()
                .setPercentDiscount(10)
                .setFixedDiscount(5)
                .setMaxDiscount(15)
                .setSupermarketName("Supermarket")
                .setIdProduct("123")
                .build();
        when(productCouponRepository.findById(randomId)).thenReturn(Optional.of(mockCoupon));

        ProductCoupon result = productCouponService.findById(randomId.toString());

        assertNotNull(result);
        assertEquals(mockCoupon, result);
    }

    @Test
    void testFindById_CouponDoesNotExist() {
        when(productCouponRepository.findById(randomId)).thenReturn(Optional.empty());

        String randomIdString = randomId.toString();
        Exception exception = assertThrows(RuntimeException.class, () ->
                productCouponService.findById(randomIdString)
        );

        assertEquals(ErrorStatus.COUPON_NOT_FOUND.getValue(), exception.getMessage());
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

        Exception exception = assertThrows(RuntimeException.class, () -> productCouponService.findByIdProduct(idProduct));

        assertEquals(ErrorStatus.COUPON_NOT_FOUND.getValue(), exception.getMessage());
    }
}
