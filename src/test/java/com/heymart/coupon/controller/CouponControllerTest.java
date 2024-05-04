package com.heymart.coupon.controller;

import com.heymart.coupon.dto.CouponRequest;
import com.heymart.coupon.model.ProductCoupon;
import com.heymart.coupon.model.TransactionCoupon;
import com.heymart.coupon.model.builder.ProductCouponBuilder;
import com.heymart.coupon.model.builder.TransactionCouponBuilder;
import com.heymart.coupon.service.ProductCouponServiceImpl;
import com.heymart.coupon.service.TransactionCouponServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
public class CouponControllerTest {
    MockMvc mockMvc;
    @Mock
    private ProductCouponServiceImpl productCouponService;
    @Mock
    private TransactionCouponServiceImpl transactionCouponService;

    @InjectMocks
    private CouponController couponController;

    @Test
    public void testCreateProductCoupon() {
        CouponRequest request = new CouponRequest();

        request.setPercentDiscount(10);
        request.setFixedDiscount(5);
        request.setMaxDiscount(15);
        request.setSupermarketName("Supermarket");
        request.setIdProduct("123");
        request.setMinTransaction(0);

        ProductCoupon coupon = new ProductCouponBuilder()
                .setPercentDiscount(10)
                .setFixedDiscount(5)
                .setMaxDiscount(15)
                .setSupermarketName("Supermarket")
                .setIdProduct("123")
                .build();
        when(productCouponService.createCoupon(request)).thenReturn(coupon);

        ResponseEntity<ProductCoupon> response = couponController.createProductCoupon(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(coupon, response.getBody());
        verify(productCouponService, times(1)).createCoupon(request);
    }

    @Test
    public void testCreateTransactionCoupon() {
        CouponRequest request = new CouponRequest(null,10, 5, 15, "Supermarket", null,0);

        TransactionCoupon coupon = new TransactionCouponBuilder()
                .setPercentDiscount(10)
                .setFixedDiscount(5)
                .setMaxDiscount(20)
                .setSupermarketName("Supermarket")
                .setMinTransaction(50)
                .build();
        when(transactionCouponService.createCoupon(request)).thenReturn(coupon);

        ResponseEntity<TransactionCoupon> response = couponController.createTransactionCoupon(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(coupon, response.getBody());
        verify(transactionCouponService, times(1)).createCoupon(request);
    }
    @Test
    public void testUpdateProductCoupon() {
        CouponRequest request = new CouponRequest(null,10, 5, 15, "Supermarket", "123",0);
        ProductCoupon coupon = new ProductCouponBuilder()
                .setPercentDiscount(10)
                .setFixedDiscount(5)
                .setMaxDiscount(15)
                .setSupermarketName("Supermarket")
                .setIdProduct("123")
                .build();
        couponController.createProductCoupon(request);
        coupon.setPercentDiscount(20);
        request.setId(coupon.getId());
        when(productCouponService.updateCoupon(request)).thenReturn(coupon);
        ResponseEntity<ProductCoupon> response = couponController.updateProductCoupon(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(coupon, response.getBody());
        verify(productCouponService, times(1)).updateCoupon(request);
    }

    @Test
    public void testUpdateTransactionCoupon() {
        CouponRequest request = new CouponRequest(null,10, 5, 15, "Supermarket", "123",0);
        TransactionCoupon coupon = new TransactionCouponBuilder()
                .setPercentDiscount(10)
                .setFixedDiscount(5)
                .setMaxDiscount(15)
                .setSupermarketName("Supermarket")
                .setMinTransaction(10)
                .build();

        couponController.createTransactionCoupon(request);
        coupon.setPercentDiscount(20);
        request.setId(coupon.getId());
        when(transactionCouponService.updateCoupon(request)).thenReturn(coupon);
        ResponseEntity<TransactionCoupon> response = couponController.updateTransactionCoupon(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(coupon, response.getBody());
        verify(transactionCouponService, times(1)).updateCoupon(request);
    }

    @Test
    public void testDeleteProductCoupon() {
        CouponRequest request = new CouponRequest(null,10, 5, 15, "Supermarket", "123",0);

        ResponseEntity<Void> response = couponController.deleteProductCoupon(request);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(productCouponService, times(1)).deleteCoupon(request);
    }

    @Test
    public void testDeleteTransactionCoupon() {
        CouponRequest request = new CouponRequest(null,10, 5, 15, "Supermarket", "123",0);

        ResponseEntity<Void> response = couponController.deleteTransactionCoupon(request);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(transactionCouponService, times(1)).deleteCoupon(request);
    }

    @Test
    public void testFindAllProductCoupons() {
        ProductCoupon coupon1 = new ProductCouponBuilder()
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
                .setIdProduct("12345")
                .build();
        List<ProductCoupon> coupons = Arrays.asList(coupon1, coupon2);
        when(productCouponService.findAllCoupons()).thenReturn(coupons);

        ResponseEntity<List<ProductCoupon>> response = couponController.findAllProductCoupons();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(coupons, response.getBody());
        verify(productCouponService, times(1)).findAllCoupons();
    }

    @Test
    public void testFindAllTransactionCoupons() {
        TransactionCoupon coupon1 = new TransactionCouponBuilder()
                .setPercentDiscount(10)
                .setFixedDiscount(5)
                .setMaxDiscount(20)
                .setSupermarketName("Supermarket")
                .setMinTransaction(50)
                .build();
        TransactionCoupon coupon2 = new TransactionCouponBuilder()
                .setPercentDiscount(10)
                .setFixedDiscount(5)
                .setMaxDiscount(20)
                .setSupermarketName("Supermarket")
                .setMinTransaction(69)
                .build();
        List<TransactionCoupon> coupons = Arrays.asList(coupon1,coupon2);
        when(transactionCouponService.findAllCoupons()).thenReturn(coupons);

        ResponseEntity<List<TransactionCoupon>> response = couponController.findAllTransactionCoupons();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(coupons, response.getBody());
        verify(transactionCouponService, times(1)).findAllCoupons();
    }
}
