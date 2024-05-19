package com.heymart.coupon.controller;
import com.heymart.coupon.dto.CouponRequest;
import com.heymart.coupon.model.TransactionCoupon;
import com.heymart.coupon.model.builder.TransactionCouponBuilder;
import com.heymart.coupon.service.AuthServiceClient;
import com.heymart.coupon.service.coupon.CouponService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TransactionCouponControllerTest {

    @InjectMocks
    private TransactionCouponController transactionCouponController;

    @Mock
    private AuthServiceClient authServiceClient;

    @Mock
    private CouponService<TransactionCoupon> transactionCouponService;

    private TransactionCoupon coupon;
    private TransactionCoupon coupon2;
    @BeforeEach
    void setUp() {
        coupon = new TransactionCouponBuilder()
                .setPercentDiscount(10)
                .setFixedDiscount(5)
                .setMaxDiscount(15)
                .setSupermarketName("Supermarket")
                .setMinTransaction(5)
                .build();
        coupon2 = new TransactionCouponBuilder()
                .setPercentDiscount(10)
                .setFixedDiscount(5)
                .setMaxDiscount(15)
                .setSupermarketName("Supermarket2")
                .setMinTransaction(5)
                .build();
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateCouponUnauthorizedUser() {
        CouponRequest request = new CouponRequest();
        when(authServiceClient.verifyUserAuthorization("coupon:create", "authHeader")).thenReturn(false);

        CompletableFuture<ResponseEntity<Object>> response = transactionCouponController.createCoupon(request, "authHeader");

        assertEquals(HttpStatus.UNAUTHORIZED, response.join().getStatusCode());
        assertEquals("Unauthorized", response.join().getBody());
    }

    @Test
    public void testCreateCouponUnauthorizedSupermarket() {
        CouponRequest request = new CouponRequest();
        when(authServiceClient.verifyUserAuthorization("coupon:create", "authHeader")).thenReturn(true);
        when(authServiceClient.verifySupermarket("authHeader", request.getSupermarketName())).thenReturn(false);

        CompletableFuture<ResponseEntity<Object>> response = transactionCouponController.createCoupon(request, "authHeader");

        assertEquals(HttpStatus.UNAUTHORIZED, response.join().getStatusCode());
        assertEquals("Unauthorized", response.join().getBody());
    }

    @Test
    public void testCreateCouponSuccess() {
        CouponRequest request = new CouponRequest();

        when(authServiceClient.verifyUserAuthorization("coupon:create", "authHeader")).thenReturn(true);
        when(authServiceClient.verifySupermarket("authHeader", request.getSupermarketName())).thenReturn(true);
        when(transactionCouponService.createCoupon(request)).thenReturn(CompletableFuture.completedFuture(coupon));

        CompletableFuture<ResponseEntity<Object>> response = transactionCouponController.createCoupon(request, "authHeader");

        assertEquals(HttpStatus.OK, response.join().getStatusCode());
        assertEquals(coupon, response.join().getBody());
    }

    @Test
    public void testFindAllCouponsUnauthorizedUser() {
        when(authServiceClient.verifyUserAuthorization("coupon:read", "authHeader")).thenReturn(false);

        ResponseEntity<Object> response = transactionCouponController.findAll("authHeader");

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("Unauthorized", response.getBody());
    }

    @Test
    public void testFindAllCouponsSuccess() {
        List<TransactionCoupon> coupons = Arrays.asList(coupon, coupon2);

        when(authServiceClient.verifyUserAuthorization("coupon:read", "authHeader")).thenReturn(true);
        when(transactionCouponService.findAllCoupons()).thenReturn(coupons);

        ResponseEntity<Object> response = transactionCouponController.findAll("authHeader");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(coupons, response.getBody());
    }

    @Test
    public void testFindCouponByIdUnauthorizedUser() {
        when(authServiceClient.verifyUserAuthorization("coupon:read", "authHeader")).thenReturn(false);

        ResponseEntity<Object> response = transactionCouponController.findById("authHeader", "couponId");

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("Unauthorized", response.getBody());
    }

    @Test
    public void testFindCouponByIdNotFound() {
        when(authServiceClient.verifyUserAuthorization("coupon:read", "authHeader")).thenReturn(true);
        when(transactionCouponService.findById("couponId")).thenThrow(new RuntimeException("Coupon not found"));

        ResponseEntity<Object> response = transactionCouponController.findById("authHeader", "couponId");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Coupon not found", response.getBody());
    }

    @Test
    public void testFindCouponByIdSuccess() {
        when(authServiceClient.verifyUserAuthorization("coupon:read", "authHeader")).thenReturn(true);
        when(transactionCouponService.findById("couponId")).thenReturn(coupon);

        ResponseEntity<Object> response = transactionCouponController.findById("authHeader", "couponId");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(coupon, response.getBody());
    }

    @Test
    public void testFindCouponsBySupermarketNameUnauthorizedUser() {
        when(authServiceClient.verifyUserAuthorization("coupon:read", "authHeader")).thenReturn(false);

        ResponseEntity<Object> response = transactionCouponController.findBySupermarketName("authHeader", "supermarketName");

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("Unauthorized", response.getBody());
    }

    @Test
    public void testFindCouponsBySupermarketNameSuccess() {
        List<TransactionCoupon> coupons = Arrays.asList(coupon,coupon2);

        when(authServiceClient.verifyUserAuthorization("coupon:read", "authHeader")).thenReturn(true);
        when(transactionCouponService.findBySupermarketName("supermarketName")).thenReturn(coupons);

        ResponseEntity<Object> response = transactionCouponController.findBySupermarketName("authHeader", "supermarketName");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(coupons, response.getBody());
    }

    @Test
    public void testUpdateCouponUnauthorizedUser() {
        CouponRequest request = new CouponRequest();
        when(authServiceClient.verifyUserAuthorization("coupon:update", "authHeader")).thenReturn(false);

        CompletableFuture<ResponseEntity<Object>> response = transactionCouponController.updateCoupon(request, "authHeader");

        assertEquals(HttpStatus.UNAUTHORIZED, response.join().getStatusCode());
        assertEquals("Unauthorized", response.join().getBody());
    }

    @Test
    public void testUpdateCouponNotFound() {
        CouponRequest request = new CouponRequest();
        when(authServiceClient.verifyUserAuthorization("coupon:update", "authHeader")).thenReturn(true);
        when(transactionCouponService.findById(request.getId())).thenThrow(new RuntimeException("Coupon not found"));

        CompletableFuture<ResponseEntity<Object>> response = transactionCouponController.updateCoupon(request, "authHeader");

        assertEquals(HttpStatus.NOT_FOUND, response.join().getStatusCode());
        assertEquals("Coupon not found", response.join().getBody());
    }

    @Test
    public void testUpdateCouponUnauthorizedSupermarket() {
        CouponRequest request = new CouponRequest();

        when(authServiceClient.verifyUserAuthorization("coupon:update", "authHeader")).thenReturn(true);
        when(transactionCouponService.findById(request.getId())).thenReturn(coupon);
        when(authServiceClient.verifySupermarket("authHeader", coupon.getSupermarketName())).thenReturn(false);

        CompletableFuture<ResponseEntity<Object>> response = transactionCouponController.updateCoupon(request, "authHeader");

        assertEquals(HttpStatus.UNAUTHORIZED, response.join().getStatusCode());
        assertEquals("Unauthorized", response.join().getBody());
    }

    @Test
    public void testUpdateCouponSuccess() {
        CouponRequest request = new CouponRequest();

        when(authServiceClient.verifyUserAuthorization("coupon:update", "authHeader")).thenReturn(true);
        when(transactionCouponService.findById(request.getId())).thenReturn(coupon);
        when(authServiceClient.verifySupermarket("authHeader", coupon.getSupermarketName())).thenReturn(true);
        when(transactionCouponService.updateCoupon(request)).thenReturn(CompletableFuture.completedFuture(coupon));

        CompletableFuture<ResponseEntity<Object>> response = transactionCouponController.updateCoupon(request, "authHeader");

        assertEquals(HttpStatus.OK, response.join().getStatusCode());
        assertEquals(coupon, response.join().getBody());
    }

    @Test
    public void testDeleteCouponUnauthorizedUser() {
        CouponRequest request = new CouponRequest();
        when(authServiceClient.verifyUserAuthorization("coupon:delete", "authHeader")).thenReturn(false);

        CompletableFuture<ResponseEntity<Object>> response = transactionCouponController.deleteCoupon(request, "authHeader");

        assertEquals(HttpStatus.UNAUTHORIZED, response.join().getStatusCode());
        assertEquals("Unauthorized", response.join().getBody());
    }

    @Test
    public void testDeleteCouponNotFound() {
        CouponRequest request = new CouponRequest();
        when(authServiceClient.verifyUserAuthorization("coupon:delete", "authHeader")).thenReturn(true);
        when(transactionCouponService.findById(request.getId())).thenThrow(new RuntimeException("Coupon not found"));

        CompletableFuture<ResponseEntity<Object>> response = transactionCouponController.deleteCoupon(request, "authHeader");

        assertEquals(HttpStatus.NOT_FOUND, response.join().getStatusCode());
        assertEquals("Coupon not found", response.join().getBody());
    }

    @Test
    public void testDeleteCouponUnauthorizedSupermarket() {
        CouponRequest request = new CouponRequest();

        when(authServiceClient.verifyUserAuthorization("coupon:delete", "authHeader")).thenReturn(true);
        when(transactionCouponService.findById(request.getId())).thenReturn(coupon);
        when(authServiceClient.verifySupermarket("authHeader", coupon.getSupermarketName())).thenReturn(false);

        CompletableFuture<ResponseEntity<Object>> response = transactionCouponController.deleteCoupon(request, "authHeader");

        assertEquals(HttpStatus.UNAUTHORIZED, response.join().getStatusCode());
        assertEquals("Unauthorized", response.join().getBody());
    }

    @Test
    public void testDeleteCouponSuccess() {
        CouponRequest request = new CouponRequest();

        when(authServiceClient.verifyUserAuthorization("coupon:delete", "authHeader")).thenReturn(true);
        when(transactionCouponService.findById(request.getId())).thenReturn(coupon);
        when(authServiceClient.verifySupermarket("authHeader", coupon.getSupermarketName())).thenReturn(true);
        when(transactionCouponService.deleteCoupon(request)).thenReturn(CompletableFuture.completedFuture(null));

        CompletableFuture<ResponseEntity<Object>> response = transactionCouponController.deleteCoupon(request, "authHeader");

        assertEquals(HttpStatus.NO_CONTENT, response.join().getStatusCode());
    }
}