package com.heymart.coupon.controller;

import com.heymart.coupon.dto.CouponRequest;
import com.heymart.coupon.enums.ErrorStatus;
import com.heymart.coupon.exception.CouponNotFoundException;
import com.heymart.coupon.model.TransactionCoupon;
import com.heymart.coupon.model.builder.TransactionCouponBuilder;
import com.heymart.coupon.service.AuthServiceClient;
import com.heymart.coupon.service.UserServiceClient;
import com.heymart.coupon.service.coupon.CouponService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class TransactionCouponControllerTest {

    @InjectMocks
    private TransactionCouponController transactionCouponController;

    @Mock
    private AuthServiceClient authServiceClient;
    @Mock
    private UserServiceClient userServiceClient;

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
                .setSupermarketId("Supermarket")
                .setMinTransaction(5)
                .build();
        coupon2 = new TransactionCouponBuilder()
                .setPercentDiscount(10)
                .setFixedDiscount(5)
                .setMaxDiscount(15)
                .setSupermarketId("Supermarket2")
                .setMinTransaction(5)
                .build();
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateCouponUnauthorizedUser() {
        CouponRequest request = new CouponRequest();
        when(authServiceClient.verifyUserAuthorization("coupon:create", "authHeader")).thenReturn(false);

        ResponseEntity<Object> response = transactionCouponController.createCoupon(request, "authHeader");

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals(ErrorStatus.UNAUTHORIZED.getValue(), response.getBody());
    }

    @Test
    void testCreateCouponUnauthorizedSupermarket() {
        CouponRequest request = new CouponRequest();
        when(authServiceClient.verifyUserAuthorization("coupon:create", "authHeader")).thenReturn(true);
        when(userServiceClient.verifySupermarket("authHeader", request.getSupermarketId())).thenReturn(false);

        ResponseEntity<Object> response = transactionCouponController.createCoupon(request, "authHeader");

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals(ErrorStatus.UNAUTHORIZED.getValue(), response.getBody());
    }

    @Test
    void testCreateCouponSuccess() {
        CouponRequest request = new CouponRequest();

        when(authServiceClient.verifyUserAuthorization("coupon:create", "authHeader")).thenReturn(true);
        when(userServiceClient.verifySupermarket("authHeader", request.getSupermarketId())).thenReturn(true);
        when(transactionCouponService.createCoupon(request)).thenReturn(coupon);

        ResponseEntity<Object> response = transactionCouponController.createCoupon(request, "authHeader");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(coupon, response.getBody());
    }

    @Test
    void testFindAllCouponsUnauthorizedUser() {
        when(authServiceClient.verifyUserAuthorization("coupon:read", "authHeader")).thenReturn(false);

        ResponseEntity<Object> response = transactionCouponController.findAll("authHeader");

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals(ErrorStatus.UNAUTHORIZED.getValue(), response.getBody());
    }

    @Test
    void testFindAllCouponsSuccess() {
        List<TransactionCoupon> coupons = Arrays.asList(coupon, coupon2);

        when(authServiceClient.verifyUserAuthorization("coupon:read", "authHeader")).thenReturn(true);
        when(transactionCouponService.findAllCoupons()).thenReturn(coupons);

        ResponseEntity<Object> response = transactionCouponController.findAll("authHeader");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(coupons, response.getBody());
    }

    @Test
    void testFindCouponByIdUnauthorizedUser() {
        when(authServiceClient.verifyUserAuthorization("coupon:read", "authHeader")).thenReturn(false);

        ResponseEntity<Object> response = transactionCouponController.findById("authHeader", "couponId");

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals(ErrorStatus.UNAUTHORIZED.getValue(), response.getBody());
    }

    @Test
    void testFindCouponByIdNotFound() {
        when(authServiceClient.verifyUserAuthorization("coupon:read", "authHeader")).thenReturn(true);
        when(transactionCouponService.findById("couponId")).thenThrow(new CouponNotFoundException(ErrorStatus.COUPON_NOT_FOUND.getValue()));

        ResponseEntity<Object> response = transactionCouponController.findById("authHeader", "couponId");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(ErrorStatus.COUPON_NOT_FOUND.getValue(), response.getBody());
    }

    @Test
    void testFindCouponByIdSuccess() {
        when(authServiceClient.verifyUserAuthorization("coupon:read", "authHeader")).thenReturn(true);
        when(transactionCouponService.findById("couponId")).thenReturn(coupon);

        ResponseEntity<Object> response = transactionCouponController.findById("authHeader", "couponId");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(coupon, response.getBody());
    }

    @Test
    void testFindCouponsBySupermarketIdUnauthorizedUser() {
        when(authServiceClient.verifyUserAuthorization("coupon:read", "authHeader")).thenReturn(false);

        ResponseEntity<Object> response = transactionCouponController.findBySupermarketId("authHeader", "supermarketId");

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals(ErrorStatus.UNAUTHORIZED.getValue(), response.getBody());
    }

    @Test
    void testFindCouponsBySupermarketIdSuccess() {
        List<TransactionCoupon> coupons = Arrays.asList(coupon,coupon2);

        when(authServiceClient.verifyUserAuthorization("coupon:read", "authHeader")).thenReturn(true);
        when(transactionCouponService.findBySupermarketId("supermarketId")).thenReturn(coupons);

        ResponseEntity<Object> response = transactionCouponController.findBySupermarketId("authHeader", "supermarketId");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(coupons, response.getBody());
    }

    @Test
    void testUpdateCouponUnauthorizedUser() {
        CouponRequest request = new CouponRequest();
        when(authServiceClient.verifyUserAuthorization("coupon:update", "authHeader")).thenReturn(false);

        ResponseEntity<Object> response = transactionCouponController.updateCoupon(request, "authHeader");

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals(ErrorStatus.UNAUTHORIZED.getValue(), response.getBody());
    }

    @Test
    void testUpdateCouponNotFound() {
        CouponRequest request = new CouponRequest();
        when(authServiceClient.verifyUserAuthorization("coupon:update", "authHeader")).thenReturn(true);
        when(transactionCouponService.findById(request.getId())).thenThrow(new CouponNotFoundException(ErrorStatus.COUPON_NOT_FOUND.getValue()));

        ResponseEntity<Object> response = transactionCouponController.updateCoupon(request, "authHeader");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(ErrorStatus.COUPON_NOT_FOUND.getValue(), response.getBody());
    }

    @Test
    void testUpdateCouponUnauthorizedSupermarket() {
        CouponRequest request = new CouponRequest();

        when(authServiceClient.verifyUserAuthorization("coupon:update", "authHeader")).thenReturn(true);
        when(transactionCouponService.findById(request.getId())).thenReturn(coupon);
        when(userServiceClient.verifySupermarket("authHeader", coupon.getSupermarketId())).thenReturn(false);

        ResponseEntity<Object> response = transactionCouponController.updateCoupon(request, "authHeader");

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals(ErrorStatus.UNAUTHORIZED.getValue(), response.getBody());
    }

    @Test
    void testUpdateCouponSuccess() {
        CouponRequest request = new CouponRequest();

        when(authServiceClient.verifyUserAuthorization("coupon:update", "authHeader")).thenReturn(true);
        when(transactionCouponService.findById(request.getId())).thenReturn(coupon);
        when(userServiceClient.verifySupermarket("authHeader", coupon.getSupermarketId())).thenReturn(true);
        when(transactionCouponService.updateCoupon(request)).thenReturn(coupon);

        ResponseEntity<Object> response = transactionCouponController.updateCoupon(request, "authHeader");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(coupon, response.getBody());
    }

    @Test
    void testDeleteCouponUnauthorizedUser() {
        CouponRequest request = new CouponRequest();
        when(authServiceClient.verifyUserAuthorization("coupon:delete", "authHeader")).thenReturn(false);

        CompletableFuture<ResponseEntity<Object>> response = transactionCouponController.deleteCoupon(request, "authHeader");

        assertEquals(HttpStatus.UNAUTHORIZED, response.join().getStatusCode());
        assertEquals(ErrorStatus.UNAUTHORIZED.getValue(), response.join().getBody());
    }

    @Test
    void testDeleteCouponNotFound() {
        CouponRequest request = new CouponRequest();
        when(authServiceClient.verifyUserAuthorization("coupon:delete", "authHeader")).thenReturn(true);
        when(transactionCouponService.findById(request.getId())).thenThrow(new CouponNotFoundException(ErrorStatus.COUPON_NOT_FOUND.getValue()));

        CompletableFuture<ResponseEntity<Object>> response = transactionCouponController.deleteCoupon(request, "authHeader");

        assertEquals(HttpStatus.NOT_FOUND, response.join().getStatusCode());
        assertEquals(ErrorStatus.COUPON_NOT_FOUND.getValue(), response.join().getBody());
    }

    @Test
    void testDeleteCouponUnauthorizedSupermarket() {
        CouponRequest request = new CouponRequest();

        when(authServiceClient.verifyUserAuthorization("coupon:delete", "authHeader")).thenReturn(true);
        when(transactionCouponService.findById(request.getId())).thenReturn(coupon);
        when(userServiceClient.verifySupermarket("authHeader", coupon.getSupermarketId())).thenReturn(false);

        CompletableFuture<ResponseEntity<Object>> response = transactionCouponController.deleteCoupon(request, "authHeader");

        assertEquals(HttpStatus.UNAUTHORIZED, response.join().getStatusCode());
        assertEquals(ErrorStatus.UNAUTHORIZED.getValue(), response.join().getBody());
    }

    @Test
    void testDeleteCouponSuccess() {
        CouponRequest request = new CouponRequest();

        when(authServiceClient.verifyUserAuthorization("coupon:delete", "authHeader")).thenReturn(true);
        when(transactionCouponService.findById(request.getId())).thenReturn(coupon);
        when(userServiceClient.verifySupermarket("authHeader", coupon.getSupermarketId())).thenReturn(true);
        when(transactionCouponService.deleteCoupon(request)).thenReturn(CompletableFuture.completedFuture(null));

        CompletableFuture<ResponseEntity<Object>> response = transactionCouponController.deleteCoupon(request, "authHeader");

        assertEquals(HttpStatus.NO_CONTENT, response.join().getStatusCode());
    }
}