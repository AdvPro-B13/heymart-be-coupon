package com.heymart.coupon.controller;

import com.heymart.coupon.dto.CouponRequest;
import com.heymart.coupon.enums.CouponAction;
import com.heymart.coupon.enums.ErrorStatus;
import com.heymart.coupon.exception.CouponAlreadyUsedException;
import com.heymart.coupon.exception.CouponNotFoundException;
import com.heymart.coupon.model.TransactionCoupon;
import com.heymart.coupon.model.UsedCoupon;
import com.heymart.coupon.model.builder.TransactionCouponBuilder;
import com.heymart.coupon.service.AuthServiceClientImpl;
import com.heymart.coupon.service.UserServiceClientImpl;
import com.heymart.coupon.service.coupon.CouponService;
import com.heymart.coupon.service.coupon.UsedCouponService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class TransactionCouponControllerTest {

    @InjectMocks
    private TransactionCouponController transactionCouponController;
    private MockMvc mockMvc;
    @Mock
    private AuthServiceClientImpl authServiceClientImpl;
    @Mock
    private UserServiceClientImpl userServiceClientImpl;
    @Mock
    private UsedCouponService usedCouponService;

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
        mockMvc = MockMvcBuilders.standaloneSetup(transactionCouponController).build();
    }

    @Test
    void testCreateCouponUnauthorizedUser() {
        CouponRequest request = new CouponRequest();
        when(authServiceClientImpl.verifyUserAuthorization("coupon:create", "authHeader")).thenReturn(false);

        ResponseEntity<Object> response = transactionCouponController.createCoupon(request, "authHeader");

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals(ErrorStatus.UNAUTHORIZED.getValue(), response.getBody());
    }

    @Test
    void testCreateCouponUnauthorizedSupermarket() {
        CouponRequest request = new CouponRequest();
        when(authServiceClientImpl.verifyUserAuthorization("coupon:create", "authHeader")).thenReturn(true);
        when(userServiceClientImpl.verifySupermarket("authHeader", request.getSupermarketId())).thenReturn(false);

        ResponseEntity<Object> response = transactionCouponController.createCoupon(request, "authHeader");

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals(ErrorStatus.UNAUTHORIZED.getValue(), response.getBody());
    }

    @Test
    void testCreateCouponSuccess() {
        CouponRequest request = new CouponRequest();

        when(authServiceClientImpl.verifyUserAuthorization("coupon:create", "authHeader")).thenReturn(true);
        when(userServiceClientImpl.verifySupermarket("authHeader", request.getSupermarketId())).thenReturn(true);
        when(transactionCouponService.createCoupon(request)).thenReturn(coupon);

        ResponseEntity<Object> response = transactionCouponController.createCoupon(request, "authHeader");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(coupon, response.getBody());
    }

    @Test
    void testFindAllCouponsUnauthorizedUser() {
        when(authServiceClientImpl.verifyUserAuthorization("coupon:read", "authHeader")).thenReturn(false);

        ResponseEntity<Object> response = transactionCouponController.findAll("authHeader");

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals(ErrorStatus.UNAUTHORIZED.getValue(), response.getBody());
    }

    @Test
    void testFindAllCouponsSuccess() {
        List<TransactionCoupon> coupons = Arrays.asList(coupon, coupon2);

        when(authServiceClientImpl.verifyUserAuthorization("coupon:read", "authHeader")).thenReturn(true);
        when(transactionCouponService.findAllCoupons()).thenReturn(coupons);

        ResponseEntity<Object> response = transactionCouponController.findAll("authHeader");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(coupons, response.getBody());
    }

    @Test
    void testFindCouponByIdUnauthorizedUser() {
        when(authServiceClientImpl.verifyUserAuthorization("coupon:read", "authHeader")).thenReturn(false);

        ResponseEntity<Object> response = transactionCouponController.findById("authHeader", "couponId");

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals(ErrorStatus.UNAUTHORIZED.getValue(), response.getBody());
    }

    @Test
    void testFindCouponByIdNotFound() {
        when(authServiceClientImpl.verifyUserAuthorization("coupon:read", "authHeader")).thenReturn(true);
        when(transactionCouponService.findById("couponId")).thenThrow(new CouponNotFoundException(ErrorStatus.COUPON_NOT_FOUND.getValue()));

        ResponseEntity<Object> response = transactionCouponController.findById("authHeader", "couponId");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(ErrorStatus.COUPON_NOT_FOUND.getValue(), response.getBody());
    }

    @Test
    void testFindCouponByIdSuccess() {
        when(authServiceClientImpl.verifyUserAuthorization("coupon:read", "authHeader")).thenReturn(true);
        when(transactionCouponService.findById("couponId")).thenReturn(coupon);

        ResponseEntity<Object> response = transactionCouponController.findById("authHeader", "couponId");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(coupon, response.getBody());
    }

    @Test
    void testFindCouponsBySupermarketIdUnauthorizedUser() {
        when(authServiceClientImpl.verifyUserAuthorization("coupon:read", "authHeader")).thenReturn(false);

        ResponseEntity<Object> response = transactionCouponController.findBySupermarketId("authHeader", "supermarketId");

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals(ErrorStatus.UNAUTHORIZED.getValue(), response.getBody());
    }

    @Test
    void testFindCouponsBySupermarketIdSuccess() {
        List<TransactionCoupon> coupons = Arrays.asList(coupon,coupon2);

        when(authServiceClientImpl.verifyUserAuthorization("coupon:read", "authHeader")).thenReturn(true);
        when(transactionCouponService.findBySupermarketId("supermarketId")).thenReturn(coupons);

        ResponseEntity<Object> response = transactionCouponController.findBySupermarketId("authHeader", "supermarketId");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(coupons, response.getBody());
    }

    @Test
    void testUpdateCouponUnauthorizedUser() {
        CouponRequest request = new CouponRequest();
        when(authServiceClientImpl.verifyUserAuthorization("coupon:update", "authHeader")).thenReturn(false);

        ResponseEntity<Object> response = transactionCouponController.updateCoupon(request, "authHeader");

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals(ErrorStatus.UNAUTHORIZED.getValue(), response.getBody());
    }

    @Test
    void testUpdateCouponNotFound() {
        CouponRequest request = new CouponRequest();
        when(authServiceClientImpl.verifyUserAuthorization("coupon:update", "authHeader")).thenReturn(true);
        when(transactionCouponService.findById(request.getId())).thenThrow(new CouponNotFoundException(ErrorStatus.COUPON_NOT_FOUND.getValue()));

        ResponseEntity<Object> response = transactionCouponController.updateCoupon(request, "authHeader");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(ErrorStatus.COUPON_NOT_FOUND.getValue(), response.getBody());
    }

    @Test
    void testUpdateCouponUnauthorizedSupermarket() {
        CouponRequest request = new CouponRequest();

        when(authServiceClientImpl.verifyUserAuthorization("coupon:update", "authHeader")).thenReturn(true);
        when(transactionCouponService.findById(request.getId())).thenReturn(coupon);
        when(userServiceClientImpl.verifySupermarket("authHeader", coupon.getSupermarketId())).thenReturn(false);

        ResponseEntity<Object> response = transactionCouponController.updateCoupon(request, "authHeader");

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals(ErrorStatus.UNAUTHORIZED.getValue(), response.getBody());
    }

    @Test
    void testUpdateCouponSuccess() {
        CouponRequest request = new CouponRequest();

        when(authServiceClientImpl.verifyUserAuthorization("coupon:update", "authHeader")).thenReturn(true);
        when(transactionCouponService.findById(request.getId())).thenReturn(coupon);
        when(userServiceClientImpl.verifySupermarket("authHeader", coupon.getSupermarketId())).thenReturn(true);
        when(transactionCouponService.updateCoupon(request)).thenReturn(coupon);

        ResponseEntity<Object> response = transactionCouponController.updateCoupon(request, "authHeader");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(coupon, response.getBody());
    }

    @Test
    void testDeleteCouponUnauthorizedUser() {
        CouponRequest request = new CouponRequest();
        when(authServiceClientImpl.verifyUserAuthorization("coupon:delete", "authHeader")).thenReturn(false);

        CompletableFuture<ResponseEntity<Object>> response = transactionCouponController.deleteCoupon(request, "authHeader");

        assertEquals(HttpStatus.UNAUTHORIZED, response.join().getStatusCode());
        assertEquals(ErrorStatus.UNAUTHORIZED.getValue(), response.join().getBody());
    }

    @Test
    void testDeleteCouponNotFound() {
        CouponRequest request = new CouponRequest();
        when(authServiceClientImpl.verifyUserAuthorization("coupon:delete", "authHeader")).thenReturn(true);
        when(transactionCouponService.findById(request.getId())).thenThrow(new CouponNotFoundException(ErrorStatus.COUPON_NOT_FOUND.getValue()));

        CompletableFuture<ResponseEntity<Object>> response = transactionCouponController.deleteCoupon(request, "authHeader");

        assertEquals(HttpStatus.NOT_FOUND, response.join().getStatusCode());
        assertEquals(ErrorStatus.COUPON_NOT_FOUND.getValue(), response.join().getBody());
    }

    @Test
    void testDeleteCouponUnauthorizedSupermarket() {
        CouponRequest request = new CouponRequest();

        when(authServiceClientImpl.verifyUserAuthorization("coupon:delete", "authHeader")).thenReturn(true);
        when(transactionCouponService.findById(request.getId())).thenReturn(coupon);
        when(userServiceClientImpl.verifySupermarket("authHeader", coupon.getSupermarketId())).thenReturn(false);

        CompletableFuture<ResponseEntity<Object>> response = transactionCouponController.deleteCoupon(request, "authHeader");

        assertEquals(HttpStatus.UNAUTHORIZED, response.join().getStatusCode());
        assertEquals(ErrorStatus.UNAUTHORIZED.getValue(), response.join().getBody());
    }

    @Test
    void testDeleteCouponSuccess() {
        CouponRequest request = new CouponRequest();

        when(authServiceClientImpl.verifyUserAuthorization("coupon:delete", "authHeader")).thenReturn(true);
        when(transactionCouponService.findById(request.getId())).thenReturn(coupon);
        when(userServiceClientImpl.verifySupermarket("authHeader", coupon.getSupermarketId())).thenReturn(true);
        when(transactionCouponService.deleteCoupon(request)).thenReturn(CompletableFuture.completedFuture(null));
        when(usedCouponService.deleteUsedCouponsByCouponId(request)).thenReturn(CompletableFuture.completedFuture(null));

        CompletableFuture<ResponseEntity<Object>> response = transactionCouponController.deleteCoupon(request, "authHeader");

        assertEquals(HttpStatus.OK, response.join().getStatusCode());
    }
    @Test
    void testDeleteCouponInternalServerError() {
        CouponRequest request = new CouponRequest();
        request.setId("test-id");

        when(authServiceClientImpl.verifyUserAuthorization("coupon:delete", "authHeader")).thenReturn(true);
        when(transactionCouponService.findById(request.getId())).thenReturn(coupon);
        when(userServiceClientImpl.verifySupermarket("authHeader", coupon.getSupermarketId())).thenReturn(true);
        when(transactionCouponService.deleteCoupon(request)).thenReturn(CompletableFuture.failedFuture(new RuntimeException("Unexpected error")));
        when(usedCouponService.deleteUsedCouponsByCouponId(request)).thenReturn(CompletableFuture.completedFuture(null));

        CompletableFuture<ResponseEntity<Object>> response = transactionCouponController.deleteCoupon(request, "authHeader");

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.join().getStatusCode());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), response.join().getBody());
    }
    @Test
    void testDeleteCouponNotFoundInExceptionally() {
        CouponRequest request = new CouponRequest();
        request.setId("test-id");

        when(authServiceClientImpl.verifyUserAuthorization("coupon:delete", "authHeader")).thenReturn(true);
        when(transactionCouponService.findById(request.getId())).thenReturn(coupon);
        when(userServiceClientImpl.verifySupermarket("authHeader", coupon.getSupermarketId())).thenReturn(true);
        when(transactionCouponService.deleteCoupon(request)).thenReturn(CompletableFuture.failedFuture(new CouponNotFoundException("Coupon not found")));
        when(usedCouponService.deleteUsedCouponsByCouponId(request)).thenReturn(CompletableFuture.completedFuture(null));

        CompletableFuture<ResponseEntity<Object>> response = transactionCouponController.deleteCoupon(request, "authHeader");

        assertEquals(HttpStatus.NOT_FOUND, response.join().getStatusCode());
        assertEquals(ErrorStatus.COUPON_NOT_FOUND.getValue(), response.join().getBody());
    }

    @Test
    void testUseCoupon_Unauthorized() throws Exception {
        when(authServiceClientImpl.verifyUserAuthorization(anyString(), anyString())).thenReturn(false);

        mockMvc.perform(post("/api/transaction-coupon/use")
                        .header("Authorization", "Bearer token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"id\": \"1\" }"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testUseCoupon_CouponNotFound() throws Exception {
        when(authServiceClientImpl.verifyUserAuthorization(anyString(), anyString())).thenReturn(true);
        when(transactionCouponService.findById("1")).thenThrow(new CouponNotFoundException(ErrorStatus.COUPON_ALREADY_USED.getValue()));

        mockMvc.perform(post("/api/transaction-coupon/use")
                        .header("Authorization", "Bearer token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"id\": \"1\" }"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testUseCoupon_CouponAlreadyUsed() throws Exception {
        when(authServiceClientImpl.verifyUserAuthorization(anyString(), anyString())).thenReturn(true);
        when(transactionCouponService.findById("1")).thenReturn(coupon);
        when(usedCouponService.useCoupon(eq(coupon), anyLong())).thenThrow(new CouponAlreadyUsedException(ErrorStatus.COUPON_ALREADY_USED.getValue()));

        mockMvc.perform(post("/api/transaction-coupon/use")
                        .header("Authorization", "Bearer token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"id\": \"1\" }"))
                .andExpect(status().isForbidden())
                .andExpect(result -> assertEquals(ErrorStatus.COUPON_ALREADY_USED.getValue(), result.getResponse().getContentAsString()));
    }


    @Test
    void testUseCoupon_Success() throws Exception {
        when(authServiceClientImpl.verifyUserAuthorization(anyString(), anyString())).thenReturn(true);
        UsedCoupon usedCoupon = new UsedCoupon();
        when(transactionCouponService.findById("1")).thenReturn(coupon);
        when(usedCouponService.useCoupon(eq(coupon),anyLong())).thenReturn(usedCoupon);

        mockMvc.perform(post("/api/transaction-coupon/use")
                        .header("Authorization", "Bearer token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"id\": \"1\" }"))
                .andExpect(status().isOk());
    }

    @Test
    void testFindUsedCouponsBySupermarketId_Unauthorized() {
        String authorizationHeader = "authHeader";
        String supermarketId = "supermarketId";

        when(authServiceClientImpl.verifyUserAuthorization(CouponAction.READ.getValue(), authorizationHeader)).thenReturn(false);

        ResponseEntity<Object> response = transactionCouponController.findUsedCouponsBySupermarketId(authorizationHeader, supermarketId);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals(ErrorStatus.UNAUTHORIZED.getValue(), response.getBody());
    }

    @Test
    void testFindUsedCouponsBySupermarketId_Success() {
        String authorizationHeader = "authHeader";
        String supermarketId = "supermarketId";
        Long userId = 1L;
        List<UsedCoupon> usedCoupons = Arrays.asList(
                new UsedCoupon(UUID.randomUUID(), supermarketId, userId),
                new UsedCoupon(UUID.randomUUID(), supermarketId, userId)
        );

        when(authServiceClientImpl.verifyUserAuthorization(CouponAction.READ.getValue(), authorizationHeader)).thenReturn(true);
        when(userServiceClientImpl.getUserId(authorizationHeader)).thenReturn(userId);
        when(usedCouponService.getUsedCouponBySupermarket(supermarketId, userId)).thenReturn(usedCoupons);

        ResponseEntity<Object> response = transactionCouponController.findUsedCouponsBySupermarketId(authorizationHeader, supermarketId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(usedCoupons, response.getBody());
    }
}